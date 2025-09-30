package org.lifetrack.lifetrackspring.database.model.dto

import org.lifetrack.lifetrackspring.database.model.data.Billing

data class BillingRequest(
    val status: String,
    val service: String,
    val totalAmount: Double,
    val paymentMethod: String,
    val transactionId: String,
    val visitId: String
)
data class BillingResponse(
    val billingInfo: MutableList<Billing>
)
