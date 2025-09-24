package org.lifetrack.lifetrackspring.database.repository

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.Billings
import org.springframework.data.mongodb.repository.MongoRepository

interface BillingRepository: MongoRepository<Billings, ObjectId> {
    fun findBillingsByOwnerId(ownerId: ObjectId): Billings
    fun findBillingsById(id: ObjectId): Billings
    fun deleteBillingsById(id: ObjectId)
    fun deleteBillingsByOwnerId(ownerId: ObjectId)
}