package org.lifetrack.lifetrackspring.database.model.delegate

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.*
import org.springframework.stereotype.Component

@Component
class MedicalDelegateImpl : MedicalDelegate {

    override fun extractPrescriptions(medHub: MedicalHistory, visitId: String): MutableList<Prescription> =
        medHub.visits.find { it.id == ObjectId(visitId) }?.prescriptions
            ?: mutableListOf()

    override fun extractLabResults(medHub: MedicalHistory, visitId: String): MutableList<LabResult> =
        medHub.visits.find { it.id == ObjectId(visitId) }?.labResults
            ?: mutableListOf()

    override fun extractDiagnosis(medHub: MedicalHistory, visitId: String): MutableList<Diagnosis> =
        medHub.visits.find { it.id == ObjectId(visitId) }?.diagnosis
            ?: mutableListOf()

    override fun extractSurgeriesByName(medHub: MedicalHistory, surgeryName: String): MutableList<PastSurgery> =
        medHub.pastSurgeries.filter {
            it.surgeryName.equals(surgeryName, ignoreCase = true) ||
                    it.surgeryName.contains(surgeryName, ignoreCase = true)
        }.toMutableList()

    override fun extractSurgeryById(medHub: MedicalHistory, surgeryId: ObjectId): PastSurgery =
        medHub.pastSurgeries.first { it.id == surgeryId }
//            ?: throw NoSuchElementException("No surgery found with id $surgeryId")

    override fun extractChronicConditionsByName(medHub: MedicalHistory, chronicCondition: String): MutableList<ChronicCondition> =
        medHub.chronicConditions.filter {
            it.name.equals(chronicCondition, ignoreCase = true) ||
                    it.name.contains(chronicCondition, ignoreCase = true)
        }.toMutableList()

    override fun extractChronicConditionById(medHub: MedicalHistory, chronicId: ObjectId): ChronicCondition =
        medHub.chronicConditions.first{ it.id == chronicId }
}
