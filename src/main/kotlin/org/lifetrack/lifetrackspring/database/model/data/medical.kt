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

@Document("medicalVault")
data class MedicalHistory(
    @Id val id: ObjectId,
    val ownerId: ObjectId,
    val allergies: MutableList<String> = mutableListOf(),
    val chronicConditions: MutableList<ChronicCondition> = mutableListOf(),
    val pastSurgeries: MutableList<PastSurgery> = mutableListOf(),
    val familyHistory: MutableList<FamilyHistory> = mutableListOf(),
    val updatedAt: Instant
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
data class ChronicCondition(
    val id: ObjectId = ObjectId.get(),
    val name: String,
    val diagnosedAt: LocalDate? = null,
    val status: String? = null,
    val outcome: String? = null,
    val lastReviewedAt: Instant? = null,
    val notes: String? = null
)





