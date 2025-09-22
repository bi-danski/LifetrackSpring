package org.lifetrack.lifetrackspring.database.model.data

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.time.LocalDate

@Document("medicalHub")
data class MedicalHistory(
    val id: ObjectId = ObjectId.get(),
    val ownerId: ObjectId,
    val allergies: List<String> = emptyList(),
    val chronicConditions: List<String> = emptyList(),
    val pastSurgeries: List<String> = emptyList(),
    val familyHistory: List<String> = emptyList(),
    val updatedAt: Instant
)
data class Visit(
    val id: ObjectId = ObjectId.get(),
    val ownerId: ObjectId,
    val date: LocalDate,
    val department: String,
    val doctor: String,
    val reasonForVisit: String,
    val diagnosis: List<String> = emptyList(),
    val notes: String? = null,
    val visitAt: Instant,
    val prescriptions: List<Prescription>,
    val labResults: MutableList<LabResult> = mutableListOf()
)
data class Prescription(
    val id: ObjectId = ObjectId.get(),
    val visitId: ObjectId,
    val ownerId: ObjectId,
    val drugName: String,
    val dosage: String,
    val frequency: String,
    val duration: String? = null,
    val notes: String? = null,
    val prescribedAt: Instant,
)
data class LabResult(
    val id: ObjectId = ObjectId.get(),
    val visitId: String,
    val ownerId: String,
    val testName: String,
    val result: String,
    val normalRange: String? = null,
    val date: String,
    val notes: String? = null,
    val testedAt: Instant
)
@Document("insurances")
data class Insurance(
    @Id val id: ObjectId = ObjectId.get(),
    val provider: String,
    val coverage: String,
    val policyNumber: String,
    val updatedAt: Instant
)
