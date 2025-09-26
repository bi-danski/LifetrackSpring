package org.lifetrack.lifetrackspring.database.model.data

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.time.LocalDate

@Document("visits")
data class UserVisits(
    @Id val id: ObjectId,
    val ownerId: ObjectId,
    var updatedAt: Instant,
    val allVisits: MutableList<Visit> = mutableListOf()
)
data class Visit(
    val id: ObjectId = ObjectId.get(),
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
    val testName: String,
    val result: String,
    val normalRange: String,
    val date: LocalDate,
    val notes: String? = null,
    val testedAt: Instant
)
data class Diagnosis(
    val id: ObjectId = ObjectId.get(),
    val visitId: ObjectId,
    val condition: String,
    val description: String,
    val severity: String,
    val status: String,
    val codedValue: String? = null,
    val diagnosedAt: Instant,
    val updatedAt: Instant
)