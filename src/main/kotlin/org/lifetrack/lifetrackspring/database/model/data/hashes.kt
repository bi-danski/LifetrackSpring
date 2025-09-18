package org.lifetrack.lifetrackspring.database.model.data


data class Hash(
    val salt: String?,
    val passwordHash: String
)
