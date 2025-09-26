package org.lifetrack.lifetrackspring.database.model.helpers

import org.lifetrack.lifetrackspring.database.model.data.Billings
import org.lifetrack.lifetrackspring.database.model.dto.BillingResponse

fun Billings.toBillingsResponse(): BillingResponse {
    return BillingResponse(this.billingInfo)
}