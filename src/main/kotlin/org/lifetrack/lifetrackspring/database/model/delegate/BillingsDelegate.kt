package org.lifetrack.lifetrackspring.database.model.delegate

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.Billing
import org.lifetrack.lifetrackspring.database.model.data.BillingItem
import org.lifetrack.lifetrackspring.database.model.data.Billings
import org.springframework.stereotype.Component

@Component
class BillingsDelegate {

    fun extractBillingServiceById(id: ObjectId, billings: Billings): BillingItem {
        lateinit var billingItem: BillingItem
        for (billing: Billing in billings.billingInfo) {
            if (billing.id == id) {
                billingItem = billing.services
            } else {
                continue
            }
        }
        return billingItem
    }

    fun extractBillingByOwnerId(ownerId: ObjectId, billings: Billings): Billing{
        lateinit var billingHolder: Billing
        for (billing: Billing in billings.billingInfo) {
            if (billing.ownerId == ownerId) {
                billingHolder = billing
            } else {
                continue
            }
        }
        return billingHolder
    }

    fun extractBillingByVisitReferenceId(visitRefId: String, billings: Billings): Billing{
        lateinit var billByRef: Billing
        for(billing: Billing in billings.billingInfo){
            if (billing.visitRefId == visitRefId){
                billByRef = billing
            }else{
                continue
            }
        }
        return billByRef
    }

    fun extractBillingByTransactionId(transactionId: String, billings: Billings): Billing{
        lateinit var billByTransId: Billing
        for(billing: Billing in billings.billingInfo){
            if (billing.transactionId == transactionId){
                billByTransId = billing
            }else{
                continue
            }
        }
        return billByTransId
    }
}