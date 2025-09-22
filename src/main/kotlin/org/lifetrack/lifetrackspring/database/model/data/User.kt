package org.lifetrack.lifetrackspring.database.model.data

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("users")
data class User(
    @Id val id: ObjectId = ObjectId.get(),
    val fullName: String?,
    val phoneNumber: Number?,
    val userName: String,
    val emailAddress: String,
    val passwordHash: String,
    val createdAt: Instant?,
    val insuranceRefId: ObjectId? = null,
    val emergencyContact: EmergencyContact? = null,
    val billing: MutableList<Billing>? = mutableListOf(),
    val vitalReferenceId: ObjectId? = null,
    val medVisits: MutableList<Visit> = mutableListOf(),
    val medicalRecords: MedicalHistory? = null
)

@Document("usersVitals")
data class UserVital(
    @Id val id: ObjectId,
    val ownerId: ObjectId,
    val pulse: Double?,
    val bloodPressure: Double? = null,
    val bodyTemperature: Double? = null,
    val respiratoryRate: Double? = null,
    val oxygenSaturation: Double? = null,
    val lastRecordAt: Instant
)

data class EmergencyContact(
    val name: String,
    val telNumber: Number,
    val relationship: String
)