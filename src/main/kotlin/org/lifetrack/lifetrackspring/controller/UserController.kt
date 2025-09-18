package org.lifetrack.lifetrackspring.controller

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.UserDataResponse
import org.lifetrack.lifetrackspring.services.AuthService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val authService: AuthService
) {
    @GetMapping
    fun getById(@RequestParam id: String): UserDataResponse {
        return authService.getUserById(ObjectId(id))
    }

    @DeleteMapping(path= ["/{id}"])
    fun delete(@PathVariable id: String) {
        authService.deleteUser(ObjectId(id))
    }

}