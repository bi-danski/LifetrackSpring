package org.lifetrack.lifetrackspring.database.model.dto

import java.time.Instant
import java.time.LocalDate

data class PrescriptionUpdate(
    val drugName: String,
    val dosage: String,
    val frequency: String,
    val duration: String,
    val notes: String? = null
)
data class LabResultUpdate(
    val testName: String,
    val result: String,
    val normalRange: String,
    val date: LocalDate,
    val notes: String? = null,
    val testedAt: Instant
)
data class DiagnosisUpdate(
    val condition: String,
    val description: String,
    val severity: String,
    val status: String,
    val codedValue: String? = null,
    val diagnosedAt: Instant,
    val updatedAt: Instant
)
data class VisitUpdate(
    val date: LocalDate,
    val department: String,
    val doctor: String,
    val reasonForVisit: String,
    val notes: String? = null,
    val visitAt: Instant,
    val diagnosis: MutableList<DiagnosisUpdate> = mutableListOf(),
    val prescriptions: MutableList<PrescriptionUpdate> = mutableListOf(),
    val labResults: MutableList<LabResultUpdate> = mutableListOf()
)