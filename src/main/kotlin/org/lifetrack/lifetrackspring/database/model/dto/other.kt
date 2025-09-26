package org.lifetrack.lifetrackspring.database.model.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.time.Instant

data class UserDataRequest(
    val id: String?,
    val fullName: String?,
    @field:NotNull(message = "Username can't be null")
    @field:NotEmpty(message = "Username can't be empty")
    @field:NotBlank(message = "Username can't be blank")
    val userName: String,
    @field:NotBlank(message = "Password can't be blank")
    @field:NotEmpty(message = "Password can't be empty")
    @field:NotNull(message = "Password can't be null")
    val password: String,
    @field:NotEmpty(message = "Email Address can't be empty")
    @field:NotBlank(message = "Email address can't be blank")
    @field:NotNull(message="Email address can't be null")
    @field:Email(message = "Invalid email address format")
    val emailAddress: String,
    val phoneNumber: Number?,
    val accessToken: String? = null
)

data class UserDataResponse(val createdAt: Instant?)

data class LoginAuthRequest(
    @field:NotEmpty(message = "Email Address can't be empty")
    @field:NotBlank(message = "Email address can't be blank")
    @field:NotNull(message="Email address can't be null")
    @field:Email(message = "Invalid email address format")
    val emailAddress: String,
    @field:NotBlank(message = "Password can't be blank")
    @field:NotEmpty(message = "Password can't be empty")
    @field:NotNull(message = "Password can't be null")
    val password: String
)

data class InsuranceRequest(
    @field:NotNull
    @field:NotBlank
    val accessToken: String,
    @field:NotNull
    @field:NotBlank
    val provider: String,
    @field:NotNull
    @field:NotBlank
    val coverage: String,
    @field:NotNull
    @field:NotBlank
    val policyNumber: String,
    @field:NotNull
    @field:NotBlank
    val ownerId: String
)

data class BRequest(
    val accessToken: String,
    val userId: String,
)




