package org.lifetrack.lifetrackspring.database.delegate

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.Billing
import org.lifetrack.lifetrackspring.database.model.data.BillingItem
import org.lifetrack.lifetrackspring.database.model.data.Billings

interface BillingsDelegate {
    fun extractBillingServiceById(id: ObjectId, billings: Billings): BillingItem
    fun extractBillingByOwnerId(ownerId: ObjectId, billings: Billings): Billing
    fun extractBillingInfoByVisitReferenceId(visitRefId: String, billings: Billings): Billing
    fun extractBillingInfoByTransactionId(transactionId: String, billings: Billings): Billing
    fun removeBillingsInfoById(billingId: ObjectId, billings: Billings): Billings
}