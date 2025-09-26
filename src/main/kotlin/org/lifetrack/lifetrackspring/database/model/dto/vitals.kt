package org.lifetrack.lifetrackspring.database.model.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.Instant

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