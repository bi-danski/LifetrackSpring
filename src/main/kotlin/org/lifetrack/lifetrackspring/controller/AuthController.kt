package org.lifetrack.lifetrackspring.controller

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.LoginAuthRequest
import org.lifetrack.lifetrackspring.database.model.data.TokenPair
import org.lifetrack.lifetrackspring.database.model.data.UserDataRequest
import org.lifetrack.lifetrackspring.database.model.data.UserDataResponse
import org.lifetrack.lifetrackspring.services.AuthService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/register")
    fun register(@RequestBody body: UserDataRequest): UserDataResponse {
        return authService.registerUser(bodyParams = body)
    }

    @PostMapping("/login")
    fun login(@RequestBody body: LoginAuthRequest): TokenPair? {
        return  authService.loginUser(bodyParams = body)
    }
    // TODO
    // fun logout(){}


}