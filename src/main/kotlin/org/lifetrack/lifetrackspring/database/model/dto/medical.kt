package org.lifetrack.lifetrackspring.database.model.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.lifetrack.lifetrackspring.database.model.data.ChronicCondition
import org.lifetrack.lifetrackspring.database.model.data.FamilyHistory
import org.lifetrack.lifetrackspring.database.model.data.PastSurgery
import org.lifetrack.lifetrackspring.database.model.data.Visit
import java.time.Instant

data class MedicalResponse(
    val allergies: MutableList<String> = mutableListOf(),
    val chronicConditions: MutableList<ChronicCondition> = mutableListOf(),
    val pastSurgeries: MutableList<PastSurgery> = mutableListOf(),
    val familyHistory: MutableList<FamilyHistory> = mutableListOf(),
    val updatedAt: Instant,
    val visits: MutableList<Visit> = mutableListOf()
)

data class MedicalPRequest(
    val allergies: MutableList<String> = mutableListOf(),
    val chronicConditions: MutableList<ChronicCondition> = mutableListOf(),
    val pastSurgeries: MutableList<PastSurgery> = mutableListOf(),
    val familyHistory: MutableList<FamilyHistory> = mutableListOf(),
)

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

data class InsuranceResponse(
    val provider: String,
    val coverage: String,
    val policyNumber: String,
    val createdAt: Instant,
    val updatedAt: Instant
)