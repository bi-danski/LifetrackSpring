package org.lifetrack.lifetrackspring.database.repository

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.UserVisits
import org.springframework.data.mongodb.repository.MongoRepository

interface VisitRepository: MongoRepository<UserVisits, ObjectId> {
    fun existsUserVisitsByOwnerId(ownerId: ObjectId): Boolean
    fun findUserVisitsByOwnerId(ownerId: ObjectId): UserVisits
}