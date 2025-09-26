package org.lifetrack.lifetrackspring.database.model.dto

data class PrescriptionUpdate(
    val drugName: String,
    val dosage: String,
    val frequency: String,
    val duration: String? = null,
    val notes: String? = null
)