package org.lifetrack.lifetrackspring.database.delegate

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.Billing
import org.lifetrack.lifetrackspring.database.model.data.Billings
import org.springframework.stereotype.Component

@Component
class BillingsDelegateImpl: BillingsDelegate{

    override fun extractBillingServiceById(id: ObjectId, billings: Billings): String {
        return billings.billingInfo.first { it.id == id }.service
    }

    override fun extractBillingByOwnerId(ownerId: ObjectId, billings: Billings): Billing{
        return billings.billingInfo.first { it.ownerId == ownerId }
    }

    override fun extractBillingInfoByVisitReferenceId(visitRefId: String, billings: Billings): Billing{
        return billings.billingInfo.first { it.visitId == ObjectId(visitRefId) }
    }

    override fun extractBillingInfoByTransactionId(transactionId: String, billings: Billings): Billing{
        return billings.billingInfo.first { it.transactionId == transactionId }
    }

    override fun removeBillingsInfoById(billingId: ObjectId, billings: Billings): Billings{
        billings.billingInfo.removeIf { it.id == billingId }
        return billings
    }
}