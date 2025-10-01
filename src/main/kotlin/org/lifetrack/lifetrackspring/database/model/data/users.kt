package org.lifetrack.lifetrackspring.database.model.data

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("users")
data class User(
    @Id val id: ObjectId,
    val fullName: String?,
    val phoneNumber: Number?,
    val userName: String,
    val emailAddress: String,
    val passwordHash: String,
    val createdAt: Instant?,
    val insuranceRefId: ObjectId? = null,
    val emergencyContact: EmergencyContact? = null,
    val billingRefId: String? = null,
    val vitalReferenceId: ObjectId? = null,
    val userVisitsReferenceId: ObjectId? = null,
    val medicalRecords: MedicalHistory? = null
)
data class EmergencyContact(
    val name: String,
    val telNumber: Number,
    val relationship: String
)

@Document("usersVitals")
data class UserVitals(
    @Id val id: ObjectId,
    val ownerId: ObjectId,
    val pulse: Double?,
    val bloodPressure: Double?,
    val bodyTemperature: Double?,
    val respiratoryRate: Double?,
    val oxygenSaturation: Double?,
    val createdAt: Instant,
    val lastUpdatedAt: Instant
)
