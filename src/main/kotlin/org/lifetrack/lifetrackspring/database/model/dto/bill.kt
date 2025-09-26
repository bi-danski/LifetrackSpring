package org.lifetrack.lifetrackspring.database.model.dto

import org.lifetrack.lifetrackspring.database.model.data.Billing

data class BillingPRequest(
    val accessToken: String,
    val userId: String,
    val data: Billing
)
data class BillingResponse(
    val billingInfo: MutableList<Billing>
)
