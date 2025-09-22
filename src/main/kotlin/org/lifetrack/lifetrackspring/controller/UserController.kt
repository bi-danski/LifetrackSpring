package org.lifetrack.lifetrackspring.controller

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.UserDataRequest
import org.lifetrack.lifetrackspring.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService
) {
    @GetMapping
    fun getUserData(@RequestParam id: String, @RequestBody uBody: UserDataRequest): Any {
        if (uBody.accessToken.isNullOrEmpty()){
            return HttpStatus.BAD_REQUEST
        }
        return userService.findUserById(ObjectId(id), uBody.accessToken)
    }

    @DeleteMapping(path= ["/{id}"])
    fun wipeUserData(@PathVariable id: String, @RequestBody uBody: UserDataRequest): HttpStatus {
        if (uBody.accessToken.isNullOrEmpty()){
            return HttpStatus.BAD_REQUEST
        }
        return userService.deleteUser(ObjectId(id), accessToken = uBody.accessToken)
    }

    @PutMapping
    fun updateUserData(){
        // TODO to be continued...
    }

}