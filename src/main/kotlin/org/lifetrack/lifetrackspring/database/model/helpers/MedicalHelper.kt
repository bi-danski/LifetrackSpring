package org.lifetrack.lifetrackspring.database.model.helpers

import org.lifetrack.lifetrackspring.database.model.data.MedicalHistory
import org.lifetrack.lifetrackspring.database.model.data.UserVitals
import org.lifetrack.lifetrackspring.database.model.dto.MedicalResponse
import org.lifetrack.lifetrackspring.database.model.dto.VitalsResponse



fun MedicalHistory.toMedicalResponse(): MedicalResponse {
    return MedicalResponse(this.allergies, this.chronicConditions, this.pastSurgeries, this.familyHistory, this.updatedAt)
}



