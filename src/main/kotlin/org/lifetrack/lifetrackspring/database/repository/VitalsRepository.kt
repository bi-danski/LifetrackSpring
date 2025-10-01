package org.lifetrack.lifetrackspring.database.repository

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.UserVitals
import org.springframework.data.mongodb.repository.MongoRepository

interface VitalsRepository: MongoRepository<UserVitals, ObjectId> {
    fun existsUserVitalsByOwnerId(ownerId: ObjectId): Boolean
    fun findUsersVitalsByOwnerId(ownerId: ObjectId): UserVitals
    fun deleteUsersVitalsByOwnerId(ownerId: ObjectId)
}