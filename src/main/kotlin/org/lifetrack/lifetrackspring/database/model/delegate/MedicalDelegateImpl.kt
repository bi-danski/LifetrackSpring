package org.lifetrack.lifetrackspring.database.model.delegate

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.*
import org.springframework.stereotype.Component

@Component
class MedicalDelegateImpl: MedicalDelegate {

    override fun extractPrescriptions(medHub: MedicalHistory, visitId: String): MutableList<Prescription>{
        lateinit var prescriptions: MutableList<Prescription>
        for (visit: Visit in medHub.visits) {
            if (visit.id == ObjectId(visitId)) {
                prescriptions = visit.prescriptions
                break
            } else {
                continue
            }
        }
        return prescriptions
    }

    override fun extractLabResults(medHub: MedicalHistory, visitId: String): MutableList<LabResult>{
        lateinit var labResults: MutableList<LabResult>
        for (visit: Visit in medHub.visits) {
            if (visit.id == ObjectId(visitId)) {
                labResults = visit.labResults
                break
            } else {
                continue
            }
        }
        return labResults
    }

    override fun extractDiagnosis(medHub: MedicalHistory, visitId: String): MutableList<Diagnosis> {
        lateinit var diagnosis: MutableList<Diagnosis>
        for (visit: Visit in medHub.visits) {
            if (visit.id == ObjectId(visitId)) {
                diagnosis = visit.diagnosis
                break
            } else {
                continue
            }
        }
        return diagnosis
    }

    override fun extractSurgeriesByName(medHub: MedicalHistory, surgeryName: String): MutableList<PastSurgery>{
        lateinit var allSurgeries: MutableList<PastSurgery>
        for(surgery: PastSurgery in medHub.pastSurgeries){
            if (surgery.surgeryName == surgeryName || surgery.surgeryName.contains(surgeryName)){
                allSurgeries.add(surgery)
            }else{
                continue
            }
        }
        return allSurgeries
    }

    override fun extractSurgeryById(medHub: MedicalHistory, surgeryId: ObjectId): PastSurgery{
        lateinit var surGery: PastSurgery
        for(surgery: PastSurgery in medHub.pastSurgeries){
            if(surgery.id == surgeryId){
                surGery = surgery
                break
            }else{
                continue
            }
        }
        return surGery
    }

    override fun extractChronicConditionsByName(medHub: MedicalHistory, chronicCondition: String): MutableList<ChronicCondition>{
        lateinit var allChronics: MutableList<ChronicCondition>
        for(condition: ChronicCondition in medHub.chronicConditions){
            if (condition.name == chronicCondition || condition.name.contains(chronicCondition)){
                allChronics.add(condition)
            }else{
                continue
            }
        }
        return allChronics
    }

    override fun extractChronicConditionById(medHub: MedicalHistory, chronicId: ObjectId): ChronicCondition{
        lateinit var chronicCondition: ChronicCondition
        for (condition: ChronicCondition in medHub.chronicConditions){
            if(condition.id == chronicId){
                chronicCondition = condition
                break
            }else{
                continue
            }
        }
        return chronicCondition
    }
}