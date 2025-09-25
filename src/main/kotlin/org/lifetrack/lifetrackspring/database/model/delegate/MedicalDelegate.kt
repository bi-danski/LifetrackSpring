package org.lifetrack.lifetrackspring.database.model.delegate

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.*

interface MedicalDelegate {
    fun extractPrescriptions(medHub: MedicalHistory, visitId: String): MutableList<Prescription>
    fun extractLabResults(medHub: MedicalHistory, visitId: String): MutableList<LabResult>
    fun extractDiagnosis(medHub: MedicalHistory, visitId: String): MutableList<Diagnosis>
    fun extractSurgeriesByName(medHub: MedicalHistory, surgeryName: String): MutableList<PastSurgery>
    fun extractSurgeryById(medHub: MedicalHistory, surgeryId: ObjectId): PastSurgery
    fun extractChronicConditionsByName(medHub: MedicalHistory, chronicCondition: String): MutableList<ChronicCondition>
    fun extractChronicConditionById(medHub: MedicalHistory, chronicId: ObjectId): ChronicCondition
}