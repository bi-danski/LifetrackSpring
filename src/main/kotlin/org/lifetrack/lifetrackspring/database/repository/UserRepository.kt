package org.lifetrack.lifetrackspring.database.repository

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, ObjectId> {
    fun findByEmailAddress(emailAddress: String): User?
}