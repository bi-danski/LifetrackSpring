package org.lifetrack.lifetrackspring.database.model.helpers

import org.lifetrack.lifetrackspring.database.model.data.User
import org.lifetrack.lifetrackspring.database.model.data.UserVitals
import org.lifetrack.lifetrackspring.database.model.dto.UserDataResponse
import org.lifetrack.lifetrackspring.database.model.dto.VitalsResponse

fun User.toResponse(): UserDataResponse {
    return UserDataResponse(
        this.fullName,
        this.phoneNumber.toLong(),
        this.userName,
        this.emailAddress,
        this.createdAt
    )
}

fun UserVitals.toVitalsResponse(): VitalsResponse {
    return VitalsResponse(this.pulse, this.bloodPressure, this.bodyTemperature, this.respiratoryRate, this.oxygenSaturation)
}