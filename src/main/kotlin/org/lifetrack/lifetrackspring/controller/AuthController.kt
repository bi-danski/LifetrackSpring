package org.lifetrack.lifetrackspring.controller

import org.lifetrack.lifetrackspring.database.model.data.RefreshRequest
import org.lifetrack.lifetrackspring.database.model.data.TokenPair
import org.lifetrack.lifetrackspring.database.model.dto.LoginAuthRequest
import org.lifetrack.lifetrackspring.database.model.dto.UserDataResponse
import org.lifetrack.lifetrackspring.database.model.dto.UserSignUpRequest
import org.lifetrack.lifetrackspring.service.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/register")
    fun register(@RequestBody body: UserSignUpRequest): UserDataResponse {
        return authService.registerUser(bodyParams = body)
    }

    @PostMapping("/login")
    fun login(@RequestBody body: LoginAuthRequest): TokenPair? {
        return authService.loginUser(bodyParams = body)
    }

    @PostMapping("/refresh")
    fun refresh(@RequestBody body: RefreshRequest): TokenPair{
        return authService.refresh(body.token)
    }

}