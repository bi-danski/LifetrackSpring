package org.lifetrack.lifetrackspring.database.model.data

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("refreshTokens")
data class RefreshToken(
    val userId: ObjectId,
    val tokenHash: String,
    val createdAt: Instant = Instant.now(),
    @Indexed(expireAfter = "0s")
    val expiresAt: Instant
)

data class TokenPair(
    val accessToken: String,
    val refreshToken: String
)

data class RefreshRequest(
    @field:NotEmpty
    @field:NotBlank
    val token: String
)
