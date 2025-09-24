package org.lifetrack.lifetrackspring.utils

import org.lifetrack.lifetrackspring.database.model.data.*

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
//        id = id,
        createdAt = createdAt
    )
}

fun UserVitals.toVitalsResponse(): VitalsResponse{
    return VitalsResponse(
        pulse = this.pulse,
        bloodPressure = this.bloodPressure,
        bodyTemperature = this.bodyTemperature,
        respiratoryRate = this.respiratoryRate,
        oxygenSaturation = this.oxygenSaturation
    )
}

fun Billings.toBillingsResponse(): BillingResponse{
    return BillingResponse(this.billingInfo)
}