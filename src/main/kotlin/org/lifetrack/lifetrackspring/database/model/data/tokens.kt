package org.lifetrack.lifetrackspring.database.model.data

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

data class TokenPair(
    val accessToken: String,
    val refreshToken: String
)

@Document("refreshTokens")
data class RefreshToken(
    val userId: ObjectId,
    val tokenHash: String,
    val createdAt: Instant = Instant.now(),
    @Indexed(expireAfter = "0s")
    val expiresAt: Instant
    )

data class RefreshRequest(
    val token: String
)
