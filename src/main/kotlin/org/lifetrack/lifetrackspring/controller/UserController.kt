package org.lifetrack.lifetrackspring.controller

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.dto.UserDataRequest
import org.lifetrack.lifetrackspring.database.model.dto.UserDataResponse
import org.lifetrack.lifetrackspring.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService
) {
    @GetMapping
    fun getUserData(@RequestBody userBodyRequest: UserDataRequest):  UserDataResponse{
        return userService.retrieveUserDataById(ObjectId(userBodyRequest.id), userBodyRequest.accessToken)
    }

    @DeleteMapping
    fun wipeUserData( @RequestBody userBodyRequest: UserDataRequest): HttpStatus {
        if (userBodyRequest.accessToken.isEmpty()){
            return HttpStatus.BAD_REQUEST
        }
        return userService.deleteUser(ObjectId(userBodyRequest.id), accessToken = userBodyRequest.accessToken)
    }

    @PutMapping
    fun updateUserData(){
        // TODO to be continued...
    }

}