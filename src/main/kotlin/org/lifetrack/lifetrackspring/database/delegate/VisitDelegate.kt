package org.lifetrack.lifetrackspring.database.delegate

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.*
import org.lifetrack.lifetrackspring.database.model.dto.*


interface VisitDelegate {
    fun extractAllPrescriptions(visits: MutableList<Visit>): MutableList<Prescription>
    fun insertPrescription(visitId: ObjectId, prescriptionUpdate: PrescriptionUpdate, visits: UserVisits)
    fun updatePrescriptions(prescriptionId: ObjectId, visitId: ObjectId, prescriptionUpdate: PrescriptionUpdate, visits: UserVisits)
    fun removePrescription(prescriptionId: ObjectId, visitId: ObjectId, visits: UserVisits)

    fun extractAllLabResults(visits: UserVisits): MutableList<LabResult>
    fun insertLabResult(labResultUpdate: LabResultUpdate, visitId: ObjectId, visits: UserVisits)
    fun updateLabResult(labResultId: ObjectId, visitId: ObjectId, labResultUpdate: LabResultUpdate, visits: UserVisits)
    fun removeLabResult(labResultId: ObjectId, visitId: ObjectId, visits: UserVisits)

    fun extractAllDiagnosis(visits: UserVisits): MutableList<Diagnosis>
    fun insertDiagnosis(visitId: ObjectId, diagnosisUpdate: DiagnosisUpdate, visits: UserVisits)
    fun updateDiagnosis(diagnosisId: ObjectId, visitId: ObjectId, update: DiagnosisUpdate, visits: UserVisits)
    fun removeDiagnosis(diagnosisId: ObjectId, visitId: ObjectId, visits: UserVisits)

    fun insertVisit(newVisit: VisitUpdate, userVisits: UserVisits)
    fun updateVisit(visitId: ObjectId ,update: UserVisitUpdate, userVisits: UserVisits)
}