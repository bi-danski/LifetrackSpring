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
    val chronicConditions: MutableList<ChronicCondition> = mutableListOf(),
    val pastSurgeries: MutableList<PastSurgery> = mutableListOf(),
    val familyHistory: MutableList<FamilyHistory> = mutableListOf(),
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
    val notes: String? = null,
    val visitAt: Instant,
    val diagnosis: MutableList<Diagnosis> = mutableListOf(),
    val prescriptions: MutableList<Prescription> = mutableListOf(),
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
    val visitId: ObjectId,
    val ownerId: ObjectId,
    val testName: String,
    val result: String,
    val normalRange: String? = null,
    val date: LocalDate,
    val notes: String? = null,
    val testedAt: Instant
)

data class Diagnosis(
    val id: ObjectId = ObjectId.get(),
    val visitId: ObjectId,
    val ownerId: ObjectId,
    val condition: String,
    val description: String? = null,
    val severity: String? = null,
    val status: String? = null,
    val codedValue: String? = null,
    val diagnosedAt: Instant,
    val updatedAt: Instant
)

data class ChronicCondition(
    val id: ObjectId = ObjectId.get(),
    val name: String,
    val diagnosedAt: LocalDate? = null,
    val status: String? = null,
    val outcome: String? = null,
    val lastReviewedAt: Instant? = null,
    val notes: String? = null
)

data class PastSurgery(
    val id: ObjectId = ObjectId.get(),
    val surgeryName: String,
    val date: LocalDate? = null,
    val hospital: String? = null,
    val surgeon: String? = null,
    val outcome: String? = null,
    val followUpRequired: Boolean = false,
    val notes: String? = null
)

data class FamilyHistory(
    val id: ObjectId = ObjectId.get(),
    val relation: String,
    val condition: String,
    val diagnosedAge: Int? = null,
    val outcome: String? = null,
    val ageAtDeath: Int? = null,
    val causeOfDeath: String? = null,
    val notes: String? = null
)
