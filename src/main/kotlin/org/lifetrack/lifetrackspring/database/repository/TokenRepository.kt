package org.lifetrack.lifetrackspring.database.repository

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.RefreshToken
import org.springframework.data.mongodb.repository.MongoRepository

interface TokenRepository: MongoRepository<RefreshToken, ObjectId> {
    fun findByUserIdAndTokenHash(userId: ObjectId, tokenHash: String ): RefreshToken?
    fun deleteByUserIdAndTokenHash(userId: ObjectId, tokenHash: String )
}