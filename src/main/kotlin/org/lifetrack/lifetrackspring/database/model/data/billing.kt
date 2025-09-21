package org.lifetrack.lifetrackspring.database.model.data

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id

data class Billing(
    @Id val id: ObjectId = ObjectId.get(),
    val visitId: String,
    val ownerId: ObjectId,
    val items: List<BillingItem>,
    val total: Double,
    val status: String,
    val paymentMethod: String,
    val transactionId: String? = null
)

data class BillingItem(
    val service: String,
    val amount: Double
)

data class Insurance(
    val provider: String,
    val coverage: String,
    val policyNumber: String
)