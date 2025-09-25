package org.lifetrack.lifetrackspring.utils

import org.lifetrack.lifetrackspring.database.model.data.*

fun User.toRequest(): UserDataRequest{
    return UserDataRequest(
        userName = this.userName, emailAddress = this.emailAddress,
        password = this.passwordHash, phoneNumber = this.phoneNumber,
        fullName = this.fullName, id = this.id.toString()
    )
}

fun User.toResponse(): UserDataResponse{
    return UserDataResponse(
//        id = id,
        this.createdAt )
}

fun UserVitals.toVitalsResponse(): VitalsResponse{
    return VitalsResponse(this.pulse, this.bloodPressure, this.bodyTemperature, this.respiratoryRate, this.oxygenSaturation)
}

fun Billings.toBillingsResponse(): BillingResponse{
    return BillingResponse(this.billingInfo)
}

fun MedicalHistory.toMedicalResponse(): MedicalResponse{
    return MedicalResponse(this.allergies, this.chronicConditions, this.pastSurgeries, this.familyHistory, this.updatedAt, this.visits)
}