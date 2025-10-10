package org.lifetrack.lifetrackspring.service

import com.mongodb.MongoException
import com.mongodb.MongoWriteException
import jakarta.validation.Valid
import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.RefreshToken
import org.lifetrack.lifetrackspring.database.model.data.TokenPair
import org.lifetrack.lifetrackspring.database.model.data.User
import org.lifetrack.lifetrackspring.database.model.dto.LoginAuthRequest
import org.lifetrack.lifetrackspring.database.model.dto.UserSignUpRequest
import org.lifetrack.lifetrackspring.database.model.helpers.toResponse
import org.lifetrack.lifetrackspring.database.repository.TokenRepository
import org.lifetrack.lifetrackspring.database.repository.UserRepository
import org.lifetrack.lifetrackspring.security.HashEncoder
import org.lifetrack.lifetrackspring.security.TokenEncoder
import org.lifetrack.lifetrackspring.utils.EmailValidation
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.Instant
import java.util.concurrent.TimeUnit

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val hashEncoder: HashEncoder,
    private val tokenEncoder: TokenEncoder,
    private val jwtService: JwtService,
    private val emailValidation: EmailValidation
) {
    fun loginUser(@Valid bodyParams: LoginAuthRequest): TokenPair? {
        val response = userRepository.findByEmailAddress(bodyParams.emailAddress) ?: throw ResponseStatusException(HttpStatusCode.valueOf(401),"Invalid Credentials")

        if (!hashEncoder.match(bodyParams.password, response.passwordHash)){
            throw ResponseStatusException(HttpStatusCode.valueOf(401), "Invalid Credentials")
        }
        val newAccessToken =  jwtService.generateAccessToken(response.id)
        val newRefreshToken = jwtService.generateRefreshToken(response.id)

        return if (saveRefreshToken(response.id, newRefreshToken).is2xxSuccessful) {
            TokenPair(
                accessToken = newAccessToken,
                refreshToken = newRefreshToken
            )
        }else{
            null
        }
    }

    fun registerUser(@Valid bodyParams: UserSignUpRequest): HttpStatus {
        val user = User(
            id = ObjectId.get(),
            fullName = bodyParams.fullName,
            userName = bodyParams.userName,
            emailAddress = bodyParams.emailAddress,
            phoneNumber = bodyParams.phoneNumber,
            createdAt = Instant.now(),
            passwordHash = hashEncoder.hashPasswd(
                bodyParams.password
            ).passwordHash,
            updatedAt = Instant.now()
        )
        return try {
            if (!emailValidation.validateUserEmailByExpression(user.emailAddress) &&
                emailValidation.validateUserEmail(user.emailAddress)?.smtp_check != true
            ) {
                return HttpStatus.NOT_ACCEPTABLE
            }

            userRepository.save<User>(user).toResponse()
            HttpStatus.CREATED
        } catch (_: MongoWriteException) {
            HttpStatus.UNPROCESSABLE_ENTITY
        } catch (_: MongoException) {
            HttpStatus.INTERNAL_SERVER_ERROR
        }
    }

    fun saveRefreshToken(userId: ObjectId, jwtRefreshToken: String): HttpStatus{
        return try {
            val tokenHash = tokenEncoder.hashToken(jwtRefreshToken)
            val refreshTokenValidityMs: Long = TimeUnit.HOURS.toMillis(1)
            tokenRepository.save(
                RefreshToken(
                    userId = userId,
                    tokenHash = tokenHash,
                    createdAt = Instant.now(),
                    expiresAt = Instant.now().plusMillis(refreshTokenValidityMs),
                )
            )
            HttpStatus.CREATED
        }catch (_: MongoWriteException){
            HttpStatus.UNPROCESSABLE_ENTITY
        }catch (_: MongoException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }
    }

    @Transactional
    fun refresh(jwtRefreshToken: String): TokenPair{
        if(!jwtService.validateRefreshToken(jwtRefreshToken)){
            throw ResponseStatusException(HttpStatusCode.valueOf(401),"Invalid Refresh Token")
        }
        val userId = jwtService.parseUserIdFromToken(jwtRefreshToken)
        val user = userRepository.findById(ObjectId(userId)).orElseThrow {
            ResponseStatusException(HttpStatusCode.valueOf(401),"Invalid Refresh Token")
        }
        val hashedToken = tokenEncoder.hashToken(jwtRefreshToken)
        tokenRepository.findByUserIdAndTokenHash(user.id, hashedToken) ?: throw
        ResponseStatusException(HttpStatusCode.valueOf(401), "Refresh Token Not Recognized")

        tokenRepository.deleteByUserIdAndTokenHash(user.id, hashedToken)
        val newAccToken = jwtService.generateAccessToken(user.id)
        val newRefToken = jwtService.generateRefreshToken(user.id)
        if (!saveRefreshToken(user.id,newRefToken).is2xxSuccessful){
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY)
        }
        return TokenPair(newAccToken,
            newRefToken)
    }

}