package org.lifetrack.lifetrackspring.service

import com.mongodb.MongoException
import com.mongodb.MongoQueryException
import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.dto.UserDataResponse
import org.lifetrack.lifetrackspring.database.model.helpers.toResponse
import org.lifetrack.lifetrackspring.database.repository.UserRepository
import org.lifetrack.lifetrackspring.exception.ResourceNotFound
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
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

    fun retrieveUserDataById(userId: ObjectId): UserDataResponse{
        if (userRepository.existsById(userId)){
            return userRepository.findUserById(userId).toResponse()
        }else{
            throw ResourceNotFound(HttpStatus.NOT_FOUND.toString())
        }
    }


}