package org.lifetrack.lifetrackspring.utils.helpers

import org.lifetrack.lifetrackspring.database.model.data.Billings
import org.lifetrack.lifetrackspring.database.model.data.MedicalHistory
import org.lifetrack.lifetrackspring.database.model.data.UserVitals
import org.lifetrack.lifetrackspring.database.model.dto.*



fun UserVitals.toVitalsResponse(): VitalsResponse {
    return VitalsResponse(this.pulse, this.bloodPressure, this.bodyTemperature, this.respiratoryRate, this.oxygenSaturation)
}

fun Billings.toBillingsResponse(): BillingResponse {
    return BillingResponse(this.billingInfo)
}

fun MedicalHistory.toMedicalResponse(): MedicalResponse {
    return MedicalResponse(this.allergies, this.chronicConditions, this.pastSurgeries, this.familyHistory, this.updatedAt)
}



