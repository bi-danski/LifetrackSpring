package org.lifetrack.lifetrackspring.utils

import org.lifetrack.lifetrackspring.database.model.data.User
import org.lifetrack.lifetrackspring.database.model.data.UserDataRequest
import org.lifetrack.lifetrackspring.database.model.data.UserDataResponse

fun User.toRequest(): UserDataRequest{
    return UserDataRequest(
        userName = userName,
        emailAddress = emailAddress,
        password = passwordHash,
        phoneNumber = phoneNumber,
        fullName = fullName,
        id = id.toString()
    )
}

fun User.toResponse(): UserDataResponse{
    return UserDataResponse(
        id = id,
        createdAt = createdAt
    )
}