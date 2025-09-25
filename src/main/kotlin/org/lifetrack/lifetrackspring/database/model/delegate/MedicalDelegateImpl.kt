package org.lifetrack.lifetrackspring.database.model.delegate

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.*
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class MedicalDelegateImpl : MedicalDelegate {

    override fun extractPrescriptions(medHub: MedicalHistory): MutableList<Prescription> {
        val allPrescription: MutableList<Prescription> = mutableListOf()
        medHub.visits.forEach { it.prescriptions.forEach { prescription ->
            allPrescription.add(prescription) }
        }
        return allPrescription
    }

    override fun extractLabResults(medHub: MedicalHistory): MutableList<LabResult> {
        val allLabResults: MutableList<LabResult> = mutableListOf()
        medHub.visits.forEach { visit ->
            visit.labResults.forEach { allLabResults.add(it) }
        }
        return allLabResults
    }

    override fun extractDiagnosis(medHub: MedicalHistory): MutableList<Diagnosis> {
        val allDiagnosis: MutableList<Diagnosis> = mutableListOf()
        medHub.visits.forEach { it.diagnosis.forEach {  diagnosis -> allDiagnosis.add(diagnosis) } }
        return allDiagnosis
    }

    override fun addNewPrescriptions(
        prescriptionUpdate: PrescriptionUpdate,
        visitId: ObjectId,
        ownerRefId:ObjectId,
        medHub: MedicalHistory
    ) {
        val newPrescription = Prescription(
            id = ObjectId.get(),
            visitIdRef = visitId,
            ownerIdRef = ownerRefId,
            drugName = prescriptionUpdate.drugName,
            dosage = prescriptionUpdate.dosage,
            frequency = prescriptionUpdate.frequency,
            duration = prescriptionUpdate.duration,
            notes = prescriptionUpdate.notes,
            prescribedAt = Instant.now(),
        )

    }

    override fun updateLabResults(
        labResult: LabResult,
        medHub: MedicalHistory
    ) {
        TODO("Not yet implemented")
    }

    override fun updateDiagnosis(
        diagnosis: Diagnosis,
        medHub: MedicalHistory
    ) {
        TODO("Not yet implemented")
    }

    override fun removePrescription(
        prescriptionId: ObjectId,
        medHub: MedicalHistory
    ) {
        TODO("Not yet implemented")
    }

    override fun removeLabResult(
        labResultId: ObjectId,
        medHub: MedicalHistory
    ) {
        TODO("Not yet implemented")
    }

    override fun removeDiagnosis(
        diagnosisId: ObjectId,
        medHub: MedicalHistory
    ) {
        TODO("Not yet implemented")
    }

    override fun extractSurgeriesByName(medHub: MedicalHistory, surgeryName: String): MutableList<PastSurgery> =
        medHub.pastSurgeries.filter {
            it.surgeryName.equals(surgeryName, ignoreCase = true) ||
                    it.surgeryName.contains(surgeryName, ignoreCase = true)
        }.toMutableList()

    override fun extractSurgeryById(medHub: MedicalHistory, surgeryId: ObjectId): PastSurgery =
        medHub.pastSurgeries.first { it.id == surgeryId }

    override fun extractChronicConditionsByName(medHub: MedicalHistory, chronicCondition: String): MutableList<ChronicCondition> =
        medHub.chronicConditions.filter {
            it.name.equals(chronicCondition, ignoreCase = true) ||
                    it.name.contains(chronicCondition, ignoreCase = true)
        }.toMutableList()

    override fun extractChronicConditionById(medHub: MedicalHistory, chronicId: ObjectId): ChronicCondition =
        medHub.chronicConditions.first{ it.id == chronicId }

    override fun extractVisitById(medHub: MedicalHistory, visitId: ObjectId): Visit =
        medHub.visits.first { it.id == visitId }

}
