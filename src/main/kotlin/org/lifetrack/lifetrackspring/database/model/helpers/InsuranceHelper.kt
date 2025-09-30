package org.lifetrack.lifetrackspring.database.model.helpers

import org.lifetrack.lifetrackspring.database.model.data.Insurance
import org.lifetrack.lifetrackspring.database.model.dto.InsuranceResponse

fun Insurance.toInsuranceResponse(): InsuranceResponse{
    return InsuranceResponse(
        this.provider,
        this.coverage,
        this.policyNumber,
        this.createdAt,
        this.updatedAt
    )
}