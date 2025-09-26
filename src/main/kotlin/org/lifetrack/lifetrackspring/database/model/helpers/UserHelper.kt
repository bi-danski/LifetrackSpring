package org.lifetrack.lifetrackspring.database.model.helpers

import org.lifetrack.lifetrackspring.database.model.data.User
import org.lifetrack.lifetrackspring.database.model.data.UserVitals
import org.lifetrack.lifetrackspring.database.model.dto.UserDataResponse
import org.lifetrack.lifetrackspring.database.model.dto.UserSignUpRequest
import org.lifetrack.lifetrackspring.database.model.dto.VitalsResponse

fun User.toSignUpRequest(): UserSignUpRequest {
    return UserSignUpRequest(
        userName = this.userName,
        emailAddress = this.emailAddress,
        password = this.passwordHash,
        phoneNumber = this.phoneNumber,
        fullName = this.fullName
    )
}

fun User.toResponse(): UserDataResponse {
    return UserDataResponse(
        this.createdAt
    )
}

fun UserVitals.toVitalsResponse(): VitalsResponse {
    return VitalsResponse(this.pulse, this.bloodPressure, this.bodyTemperature, this.respiratoryRate, this.oxygenSaturation)
}