package org.lifetrack.lifetrackspring.database.repository

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.Billings
import org.springframework.data.mongodb.repository.MongoRepository

interface BillingRepository: MongoRepository<Billings, ObjectId> {
    fun findBillingsByOwnerId(ownerId: ObjectId): Billings
    fun deleteBillingsByOwnerId(ownerId: ObjectId)
    fun existsBillingsById(id: ObjectId): Boolean
}