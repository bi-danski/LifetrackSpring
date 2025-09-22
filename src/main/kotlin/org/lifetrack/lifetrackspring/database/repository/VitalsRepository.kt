package org.lifetrack.lifetrackspring.database.repository

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.UserVitals
import org.springframework.data.mongodb.repository.MongoRepository

interface VitalsRepository: MongoRepository<UserVitals, ObjectId> {
    fun findUsersVitalsById(id: ObjectId): UserVitals
    fun deleteUsersVitalsById(id: ObjectId)
}