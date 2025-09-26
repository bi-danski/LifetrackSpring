package org.lifetrack.lifetrackspring.database.repository

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.Visit
import org.springframework.data.mongodb.repository.MongoRepository

interface VisitRepository: MongoRepository<Visit, ObjectId> {
    fun findVisitByOwnerId(ownerId: ObjectId): Visit
    fun existsVisitsByOwnerId(ownerId: ObjectId): Boolean
}