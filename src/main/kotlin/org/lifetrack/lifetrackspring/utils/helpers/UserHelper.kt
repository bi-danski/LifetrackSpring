package org.lifetrack.lifetrackspring.utils.helpers

import org.lifetrack.lifetrackspring.database.model.data.User
import org.lifetrack.lifetrackspring.database.model.dto.UserDataResponse
import org.lifetrack.lifetrackspring.database.model.dto.UserSignUpRequest

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