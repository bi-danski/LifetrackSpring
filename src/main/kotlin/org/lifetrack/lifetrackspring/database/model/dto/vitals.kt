package org.lifetrack.lifetrackspring.database.model.dto

import java.time.Instant

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