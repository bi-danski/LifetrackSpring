package org.lifetrack.lifetrackspring.service

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.dto.UserDataResponse
import org.lifetrack.lifetrackspring.database.repository.UserRepository
import org.lifetrack.lifetrackspring.exception.ResourceNotFound
import org.lifetrack.lifetrackspring.utils.ValidationUtil
import org.lifetrack.lifetrackspring.database.model.helpers.toResponse
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
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

    fun retrieveUserDataById(id: ObjectId, accessToken: String): UserDataResponse{
        if(!validationUtil.validateRequestFromUser(id, accessToken = accessToken)){
            throw AccessDeniedException (HttpStatus.UNAUTHORIZED.toString())
        }
        val response = userRepository.findById(id)
        if(response.isEmpty ){
            throw ResourceNotFound(HttpStatus.NOT_FOUND.toString())
        }
        return response.get().toResponse()
    }
}