package org.lifetrack.lifetrackspring.services

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.repository.UserRepository
import org.lifetrack.lifetrackspring.utils.ValidationUtil
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val validationUtil: ValidationUtil
) {
    fun deleteUser(id: ObjectId, accessToken: String): HttpStatus{
        if (!validationUtil.validateRequestFromUser(id, accessToken)){
            return HttpStatus.UNAUTHORIZED
        }
        if(userRepository.findById(id).isEmpty){
            return HttpStatus.NOT_FOUND
        }
        userRepository.deleteById(id)
        return HttpStatus.OK
    }

    fun findUserById(id: ObjectId, accessToken: String): HttpStatus{
        if(!validationUtil.validateRequestFromUser(id, accessToken = accessToken)){
            return HttpStatus.UNAUTHORIZED
        }
        val response = userRepository.findById(id)
        if(response.isEmpty ){
            return HttpStatus.NOT_FOUND
        }
        return HttpStatus.FOUND
    }
}