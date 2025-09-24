package org.lifetrack.lifetrackspring.database.model.data

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("vault")
data class Billings(
    @Id val id: ObjectId = ObjectId.get(),
    val ownerId: ObjectId,
    val billingInfo: MutableList<Billing>
)

data class Billing(
    val id: ObjectId = ObjectId.get(),
    val visitRefId: String,
    val total: Double,
    val status: String,
    val services: BillingItem,
    val paymentMethod: String,
    val transactionId: String,
    val updatedAt: Instant,
    val ownerId: ObjectId
)

data class BillingItem(
    val service: String,
    val amount: Double,
    val updatedAt: Instant
)

