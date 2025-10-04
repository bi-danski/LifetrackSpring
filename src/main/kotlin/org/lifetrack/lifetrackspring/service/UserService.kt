package org.lifetrack.lifetrackspring.service

import com.mongodb.MongoException
import com.mongodb.MongoQueryException
import com.mongodb.MongoWriteException
import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.dto.UserDataResponse
import org.lifetrack.lifetrackspring.database.model.dto.UserSignUpRequest
import org.lifetrack.lifetrackspring.database.model.helpers.toResponse
import org.lifetrack.lifetrackspring.database.repository.UserRepository
import org.lifetrack.lifetrackspring.exception.ResourceNotFound
import org.lifetrack.lifetrackspring.security.HashEncoder
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class UserService(
    private val userRepository: UserRepository,
    private val hashEncoder: HashEncoder
) {
    fun deleteUser(userId: ObjectId): HttpStatus{
        return try {
            if (!userRepository.existsById(userId)) {
                return HttpStatus.NOT_FOUND
            }
            userRepository.deleteById(userId)
            HttpStatus.OK
        }catch (_: MongoQueryException){
            HttpStatus.UNPROCESSABLE_ENTITY
        }catch (_: MongoException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }
    }

    fun retrieveUserData(userId: ObjectId): UserDataResponse{
        if (userRepository.existsById(userId)){
            return userRepository.findUserById(userId).toResponse()
        }else{
            throw ResourceNotFound(HttpStatus.NOT_FOUND.toString())
        }
    }

    fun updateUserData(userId: ObjectId, userInfo: UserSignUpRequest): HttpStatus{
        return try {
            if (!userRepository.existsUserById(userId)) {
                return HttpStatus.NOT_FOUND
            }
            val existingCopy = userRepository.findUserById(userId)
            val updatedCopy = userRepository.findUserById(userId).copy(
                fullName = userInfo.fullName?.ifEmpty { existingCopy.fullName },
                emailAddress = userInfo.emailAddress.ifEmpty { existingCopy.emailAddress },
                phoneNumber = if (userInfo.phoneNumber.toString().isNotEmpty()) userInfo.phoneNumber else existingCopy.phoneNumber,
                passwordHash = if (userInfo.password.isNotEmpty()) hashEncoder.hashPasswd(userInfo.password).passwordHash else existingCopy.passwordHash,
                updatedAt = Instant.now()
            )
            userRepository.save(updatedCopy)
            HttpStatus.OK
        }catch (_: MongoException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: MongoWriteException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }
    }
}