package org.lifetrack.lifetrackspring.database.model.helpers

import org.lifetrack.lifetrackspring.database.model.data.Diagnosis
import org.lifetrack.lifetrackspring.database.model.data.LabResult
import org.lifetrack.lifetrackspring.database.model.data.MedicalHistory
import org.lifetrack.lifetrackspring.database.model.data.Prescription
import org.lifetrack.lifetrackspring.database.model.dto.DiagnosisUpdate
import org.lifetrack.lifetrackspring.database.model.dto.LabResultUpdate
import org.lifetrack.lifetrackspring.database.model.dto.MedicalResponse
import org.lifetrack.lifetrackspring.database.model.dto.PrescriptionUpdate

fun MedicalHistory.toMedicalResponse(): MedicalResponse {
    return MedicalResponse(this.allergies, this.chronicConditions, this.pastSurgeries, this.familyHistory, this.updatedAt)
}

fun Diagnosis.toDiagnosisUpdate(): DiagnosisUpdate {
    return DiagnosisUpdate(this.condition, this.description, this.severity, this.status, this.codedValue, this.diagnosedAt,
        this.updatedAt
    )
}

fun Prescription.toPrescriptionUpdate(): PrescriptionUpdate {
    return PrescriptionUpdate(this.drugName, this.dosage, this.frequency, duration = this.duration ?: "", this.notes,)
}

fun LabResult.toLabResultUpdate(): LabResultUpdate{
    return LabResultUpdate(this.testName, this.result, this.normalRange, this.date, this.notes, this.testedAt)
}
