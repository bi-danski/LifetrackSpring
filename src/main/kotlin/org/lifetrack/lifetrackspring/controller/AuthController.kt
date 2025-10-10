package org.lifetrack.lifetrackspring.controller

import jakarta.validation.Valid
import org.lifetrack.lifetrackspring.database.model.data.RefreshRequest
import org.lifetrack.lifetrackspring.database.model.data.TokenPair
import org.lifetrack.lifetrackspring.database.model.dto.LoginAuthRequest
import org.lifetrack.lifetrackspring.database.model.dto.UserSignUpRequest
import org.lifetrack.lifetrackspring.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {
    @GetMapping
    fun authResponder() = ResponseStatusException(HttpStatus.NOT_FOUND)

    @PostMapping("/register")
    fun register(@Valid @RequestBody body: UserSignUpRequest): HttpStatus {
        return authService.registerUser(bodyParams = body)
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody body: LoginAuthRequest): TokenPair? {
        val tokenPair =
            authService.loginUser(bodyParams = body) ?:
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oyaa ")
        return tokenPair
    }

    @PostMapping("/refresh")
    fun refresh(@Valid @RequestBody body: RefreshRequest): TokenPair{
        return authService.refresh(body.token)
    }

}