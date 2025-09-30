package org.lifetrack.lifetrackspring.database.model.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class InsuranceRequest(
    @field:NotNull
    @field:NotBlank
    val provider: String,
    @field:NotNull
    @field:NotBlank
    val coverage: String,
    @field:NotNull
    @field:NotBlank
    val policyNumber: String,
    @field:NotNull
    @field:NotBlank
    val ownerId: String
)

data class BRequest(
    val accessToken: String,
)




