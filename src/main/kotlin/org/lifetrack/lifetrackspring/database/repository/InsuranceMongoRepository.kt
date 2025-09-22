package org.lifetrack.lifetrackspring.database.repository

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.Insurance
import org.springframework.data.mongodb.repository.MongoRepository

interface InsuranceMongoRepository: MongoRepository<Insurance, ObjectId> {
    fun getInsuranceById(id: ObjectId): Insurance
    fun deleteInsuranceById(id: ObjectId)
}