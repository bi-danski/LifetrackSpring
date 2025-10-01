package org.lifetrack.lifetrackspring.controller

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.dto.UserDataResponse
import org.lifetrack.lifetrackspring.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService
) {
    final fun userId() = ObjectId(SecurityContextHolder.getContext().authentication.principal as String)

    @GetMapping
    fun getUserData():  UserDataResponse{
        return userService.retrieveUserDataById(userId())
    }

    @DeleteMapping
    fun wipeUserData(): HttpStatus {
        return userService.deleteUser(userId())
    }

    @PutMapping
    fun updateUserData(){
        // ToDo
    }

}