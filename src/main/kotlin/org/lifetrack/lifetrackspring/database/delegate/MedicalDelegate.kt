package org.lifetrack.lifetrackspring.database.delegate

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.ChronicCondition
import org.lifetrack.lifetrackspring.database.model.data.MedicalHistory
import org.lifetrack.lifetrackspring.database.model.data.PastSurgery

interface MedicalDelegate {
    fun extractSurgeriesByName(medHub: MedicalHistory, surgeryName: String): MutableList<PastSurgery>
    fun extractSurgeryById(medHub: MedicalHistory, surgeryId: ObjectId): PastSurgery

    fun extractChronicConditionsByName(medHub: MedicalHistory, chronicCondition: String): MutableList<ChronicCondition>
    fun extractChronicConditionById(medHub: MedicalHistory, chronicId: ObjectId): ChronicCondition

}