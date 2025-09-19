package org.lifetrack.lifetrackspring.database.model.data

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("users")
data class User(
    val fullName: String?,
    val phoneNumber: Number?,

    @field:NotNull(message = "Username can't be null")
    @field:NotEmpty(message = "Username can't be empty")
    @field:NotBlank(message = "Username can't be blank")
    val userName: String,

    @field:NotEmpty(message = "Email Address can't be empty")
    @field:NotBlank(message = "Email address can't be blank")
    @field:NotNull(message="Email address can't be null")
    @field:Email(message = "Invalid email address format")
    val emailAddress: String,

    val passwordHash: String,
    val passwordSalt: String?,
    val createdAt: Instant?,
    @Id val id: ObjectId = ObjectId.get()
)