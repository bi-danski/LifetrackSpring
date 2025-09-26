package org.lifetrack.lifetrackspring.services

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.RefreshToken
import org.lifetrack.lifetrackspring.database.model.data.TokenPair
import org.lifetrack.lifetrackspring.database.model.data.User
import org.lifetrack.lifetrackspring.database.model.dto.LoginAuthRequest
import org.lifetrack.lifetrackspring.database.model.dto.UserDataResponse
import org.lifetrack.lifetrackspring.database.model.dto.UserSignUpRequest
import org.lifetrack.lifetrackspring.database.repository.TokenRepository
import org.lifetrack.lifetrackspring.database.repository.UserRepository
import org.lifetrack.lifetrackspring.security.HashEncoder
import org.lifetrack.lifetrackspring.security.TokenEncoder
import org.lifetrack.lifetrackspring.utils.helpers.toResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.logging.Logger

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val hashEncoder: HashEncoder,
    private val tokenEncoder: TokenEncoder,
    private val jwtService: JwtService,
) {
    private val zanguZangu = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)
    fun loginUser(bodyParams: LoginAuthRequest): TokenPair? {
        val response = userRepository.findByEmailAddress(bodyParams.emailAddress) ?: throw IllegalArgumentException("Invalid Credentials")

        if (!hashEncoder.match(bodyParams.password, response.passwordHash)){
            throw IllegalArgumentException("Invalid Credentials")
        }
        val newAccessToken =  jwtService.generateAccessToken(response.id)
        val newRefreshToken = jwtService.generateRefreshToken(response.id)

        return if (saveRefreshToken(response.id, newRefreshToken)) {
            TokenPair(
                accessToken = newAccessToken,
                refreshToken = newRefreshToken
            )
        }else{
            null
        }
    }

    fun registerUser(bodyParams: UserSignUpRequest): UserDataResponse {
        val pwHash = hashEncoder.hashPasswd(bodyParams.password)
        val user = User(
            id = ObjectId.get(),
            fullName = bodyParams.fullName,
            userName = bodyParams.userName,
            emailAddress = bodyParams.emailAddress,
            phoneNumber = bodyParams.phoneNumber,
            createdAt = Instant.now(),
            passwordHash = pwHash.passwordHash,
        )
        val response = userRepository.save<User>(user)
        return response.toResponse()
    }

    fun saveRefreshToken(userId: ObjectId, jwtRefreshToken: String): Boolean{
        try{
            val tokenHash = tokenEncoder.hashToken(jwtRefreshToken)
            val refreshTokenValidityMs: Long = 2L * 24L * 60 * 60 * 1000
            tokenRepository.save(RefreshToken(
                userId = userId,
                tokenHash = tokenHash,
                createdAt = Instant.now(),
                expiresAt = Instant.now().plusMillis(refreshTokenValidityMs),
            ))
            return true
        }catch (e: Exception){
            zanguZangu.log(zanguZangu.level, e.message)
            return false
        }
    }

    @Transactional
    fun refresh(jwtRefreshToken: String): TokenPair{
        if(!jwtService.validateRefreshToken(jwtRefreshToken)){
            throw IllegalArgumentException("Invalid Refresh Token")
        }
        val userId = jwtService.parseUserIdFromToken(jwtRefreshToken)
        val user = userRepository.findById(ObjectId(userId)).orElseThrow {
            IllegalArgumentException("Invalid Refresh Token")
        }
        val hashedToken = tokenEncoder.hashToken(jwtRefreshToken)
        tokenRepository.findByUserIdAndTokenHash(user.id, hashedToken) ?: throw
            IllegalArgumentException("Refresh Token Not Recognized")

        tokenRepository.deleteByUserIdAndTokenHash(user.id, hashedToken)
        val newAccToken = jwtService.generateAccessToken(user.id)
        val newRefToken = jwtService.generateRefreshToken(user.id)
        saveRefreshToken(user.id,
            newRefToken)

        return TokenPair(newAccToken,
            newRefToken)
    }


}