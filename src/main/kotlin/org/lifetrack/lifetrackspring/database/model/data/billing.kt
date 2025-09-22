package org.lifetrack.lifetrackspring.database.model.data

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("vault")
data class Billing(
    @Id val id: ObjectId = ObjectId.get(),
    val visitId: String,
    val ownerId: ObjectId,
    val items: List<BillingItem>,
    val total: Double,
    val status: String,
    val paymentMethod: String,
    val transactionId: String? = null,
    val updatedAt: Instant
)

data class BillingItem(
    val service: String,
    val amount: Double,
    val updatedAt: Instant
)
