package org.lifetrack.lifetrackspring.database.model.data

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("vault")
data class Billings(
    @Id val id: ObjectId = ObjectId.get(),
    val ownerId: ObjectId,
    val updatedAt: Instant,
    val billingInfo: MutableList<Billing>
)

data class Billing(
    @Id val id: ObjectId,
    val visitId: ObjectId,
    val status: String,
    val service: String,
    val totalAmount: Double,
    val paymentMethod: String,
    val transactionId: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val ownerId: ObjectId
)


