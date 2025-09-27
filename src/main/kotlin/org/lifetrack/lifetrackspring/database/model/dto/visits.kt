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
data class UserVisitUpdate(
    val date: LocalDate,
    val department: String,
    val doctor: String,
    val reasonForVisit: String,
    val notes: String? = null,
    val visitAt: Instant
)
data class UserVisitRequest(
    val visitId: String,
    val userId: String,
    val accessToken: String,
    val visitInfo: UserVisitUpdate
)

data class UserPrescriptionRequest(
    val prescriptionId: String,
    val visitId: String,
    val userId: String,
    val accessToken: String,
    val prescriptionUpdate: PrescriptionUpdate,
)

data class UserLabRequest(
    val labResultId: String,
    val visitId: String,
    val userId: String,
    val accessToken: String,
    val labResultUpdate: LabResultUpdate
)

data class UserDiagnosisRequest(
    val diagnosisId: String,
    val visitId: String,
    val userId: String,
    val accessToken: String,
    val diagnosisUpdate: DiagnosisUpdate
)