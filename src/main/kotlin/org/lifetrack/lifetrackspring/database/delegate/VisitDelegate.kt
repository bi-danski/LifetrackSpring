package org.lifetrack.lifetrackspring.database.delegate

import org.lifetrack.lifetrackspring.database.model.data.Diagnosis
import org.lifetrack.lifetrackspring.database.model.data.LabResult
import org.lifetrack.lifetrackspring.database.model.data.Prescription
import org.lifetrack.lifetrackspring.database.model.data.Visit

interface VisitDelegate {
    fun extractPrescriptions(visits: MutableList<Visit>): MutableList<Prescription>
    fun extractLabResults(visits: MutableList<Visit>): MutableList<LabResult>
    fun extractDiagnosis(visits: MutableList<Visit>): MutableList<Diagnosis>

}