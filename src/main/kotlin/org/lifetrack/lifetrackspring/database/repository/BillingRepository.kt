package org.lifetrack.lifetrackspring.database.repository

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.Billing
import org.springframework.data.mongodb.repository.MongoRepository

interface BillingRepository: MongoRepository<Billing, ObjectId> {
    fun findBillingsByOwnerId(ownerId: ObjectId): MutableList<Billing>
}