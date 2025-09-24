package org.lifetrack.lifetrackspring.database.model.data

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.time.LocalDate

@Document("insurances")
data class Insurance(
    @Id val id: ObjectId,
    val provider: String,
    val coverage: String,
    val policyNumber: String,
    val updatedAt: Instant
)

@Document("medicalHub")
data class MedicalHistory(
    val id: ObjectId = ObjectId.get(),
    val ownerId: ObjectId,
    val allergies: MutableList<String> = mutableListOf(),
    val chronicConditions: MutableList<String> = mutableListOf(),
    val pastSurgeries: MutableList<Map<Any, Any>> = mutableListOf(),
    val familyHistory: MutableList<Map<Any, Any>> = mutableListOf(),
    val updatedAt: Instant,
    val visits: MutableList<Visit> = mutableListOf()
)

data class Visit(
    val id: ObjectId = ObjectId.get(),
    val ownerId: ObjectId,
    val date: LocalDate,
    val department: String,
    val doctor: String,
    val reasonForVisit: String,
    val diagnosis: MutableList<LabResult> = mutableListOf(),
    val notes: String? = null,
    val visitAt: Instant,
    val prescriptions: List<Prescription>? = null,
    val labResults: MutableList<LabResult> = mutableListOf()
)
data class Prescription(
//    val id: ObjectId = ObjectId.get(),
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


