package org.lifetrack.lifetrackspring.database.repository

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.UserVital
import org.springframework.data.mongodb.repository.MongoRepository

interface VitalsMongoRepository: MongoRepository<UserVital, ObjectId> {
    fun findUserVitalById(id: ObjectId): UserVital
//    fun updateUserVitalById(id: ObjectId)
    fun deleteInsuranceById(id: ObjectId)
}