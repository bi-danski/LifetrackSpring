package org.lifetrack.lifetrackspring.database.model.data

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

data class UserDataResponse(
//    val id: ObjectId,
    val createdAt: Instant?
)

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

data class VitalsRequest(
    val resId: String? = null,
    @field:NotNull
    @field:NotBlank
    val accessToken: String,
    val vitalsData: VitalsDataRequest? = null
)

data class VitalsDataRequest(
    val pulse: Double? = null,
    val bloodPressure: Double? = null,
    val bodyTemperature: Double? = null,
    val respiratoryRate: Double? = null,
    val oxygenSaturation: Double? = null,
    val lastRecordAt: Instant = Instant.now()
)

data class VitalsResponse(
    val pulse: Double?,
    val bloodPressure: Double? = null,
    val bodyTemperature: Double? = null,
    val respiratoryRate: Double? = null,
    val oxygenSaturation: Double? = null
)