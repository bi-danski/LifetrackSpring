package org.lifetrack.lifetrackspring.database.model.delegate

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.*

interface MedicalDelegate {
    fun extractPrescriptions(medHub: MedicalHistory): MutableList<Prescription>
    fun extractLabResults(medHub: MedicalHistory): MutableList<LabResult>
    fun extractDiagnosis(medHub: MedicalHistory): MutableList<Diagnosis>

    fun addNewPrescriptions(prescriptionUpdate: PrescriptionUpdate, visitId: ObjectId,ownerId: ObjectId, medHub: MedicalHistory)
    fun updateLabResults(labResult: LabResult, medHub: MedicalHistory)
    fun updateDiagnosis(diagnosis: Diagnosis ,medHub: MedicalHistory)

    fun removePrescription(prescriptionId: ObjectId, medHub: MedicalHistory)
    fun removeLabResult(labResultId: ObjectId, medHub: MedicalHistory)
    fun removeDiagnosis(diagnosisId: ObjectId, medHub: MedicalHistory)

    fun extractSurgeriesByName(medHub: MedicalHistory, surgeryName: String): MutableList<PastSurgery>
    fun extractSurgeryById(medHub: MedicalHistory, surgeryId: ObjectId): PastSurgery

    fun extractChronicConditionsByName(medHub: MedicalHistory, chronicCondition: String): MutableList<ChronicCondition>
    fun extractChronicConditionById(medHub: MedicalHistory, chronicId: ObjectId): ChronicCondition

    fun extractVisitById(medHub: MedicalHistory, visitId: ObjectId): Visit
}