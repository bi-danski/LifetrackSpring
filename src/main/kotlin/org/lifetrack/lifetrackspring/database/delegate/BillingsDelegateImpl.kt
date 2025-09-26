package org.lifetrack.lifetrackspring.database.delegate

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.Billing
import org.lifetrack.lifetrackspring.database.model.data.BillingItem
import org.lifetrack.lifetrackspring.database.model.data.Billings
import org.springframework.stereotype.Component

@Component
class BillingsDelegateImpl: BillingsDelegate{

    override fun extractBillingServiceById(id: ObjectId, billings: Billings): BillingItem {
        return billings.billingInfo.first { it.id == id }.services
    }

    override fun extractBillingByOwnerId(ownerId: ObjectId, billings: Billings): Billing{
        return billings.billingInfo.first { it.ownerId == ownerId }
    }

    override fun extractBillingInfoByVisitReferenceId(visitRefId: String, billings: Billings): Billing{
        return billings.billingInfo.first { it.visitRefId == visitRefId }
    }

    override fun extractBillingInfoByTransactionId(transactionId: String, billings: Billings): Billing{
        return billings.billingInfo.first { it.transactionId == transactionId }
    }

    override fun removeBillingsInfoById(billingId: ObjectId, billings: Billings): Billings{
        billings.billingInfo.removeIf { it.id == billingId }
        return billings
    }
}