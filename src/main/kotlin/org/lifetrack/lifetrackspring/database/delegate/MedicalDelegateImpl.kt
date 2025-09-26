package org.lifetrack.lifetrackspring.database.delegate

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.ChronicCondition
import org.lifetrack.lifetrackspring.database.model.data.MedicalHistory
import org.lifetrack.lifetrackspring.database.model.data.PastSurgery
import org.springframework.stereotype.Component

@Component
class MedicalDelegateImpl : MedicalDelegate {

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

}
