package org.lifetrack.lifetrackspring.database.model.data

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("users")
data class User(
    val fullName: String?,
    val phoneNumber: Number?,
    val userName: String,
    val emailAddress: String,
    val passwordHash: String,
    val passwordSalt: String?,
    val createdAt: Instant?,
    @Id val id: ObjectId = ObjectId.get()
)

data class UserDataRequest(
    val id: String?,
    val fullName: String?,
    val userName: String,
    val password: String,
    val emailAddress: String,
    val phoneNumber: Number?
)

data class UserDataResponse(
    val id: ObjectId,
    val createdAt: Instant?
)

data class LoginAuthRequest(
    val emailAddress: String,
    val password: String
)