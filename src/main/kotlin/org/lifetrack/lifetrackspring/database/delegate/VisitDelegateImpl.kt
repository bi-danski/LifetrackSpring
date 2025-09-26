package org.lifetrack.lifetrackspring.database.delegate

import org.lifetrack.lifetrackspring.database.model.data.Diagnosis
import org.lifetrack.lifetrackspring.database.model.data.LabResult
import org.lifetrack.lifetrackspring.database.model.data.Prescription
import org.lifetrack.lifetrackspring.database.model.data.Visit
import org.springframework.stereotype.Component

@Component
class VisitDelegateImpl : VisitDelegate{
    override fun extractPrescriptions(visits: MutableList<Visit>): MutableList<Prescription> {
        val allPrescription: MutableList<Prescription> = mutableListOf()
        visits.forEach { it.prescriptions.forEach { prescription ->
            allPrescription.add(prescription) }
        }
        return allPrescription
    }

    override fun extractLabResults(visits: MutableList<Visit>): MutableList<LabResult> {
        val allLabResults: MutableList<LabResult> = mutableListOf()
        visits.forEach { it.labResults.forEach { labResult -> allLabResults.add(labResult) } }
        return allLabResults
    }

    override fun extractDiagnosis(visits: MutableList<Visit>): MutableList<Diagnosis> {
        val allDiagnosis: MutableList<Diagnosis> = mutableListOf()
        visits.forEach { it.diagnosis.forEach { diagnosis -> allDiagnosis.add(diagnosis) } }
        return allDiagnosis
    }
}