package org.lifetrack.lifetrackspring.database.delegate

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.*
import org.lifetrack.lifetrackspring.database.model.dto.*
import org.lifetrack.lifetrackspring.database.model.helpers.toDiagnosisUpdate
import org.lifetrack.lifetrackspring.database.model.helpers.toLabResultUpdate
import org.lifetrack.lifetrackspring.database.model.helpers.toPrescriptionUpdate
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class VisitDelegateImpl : VisitDelegate {

    override fun extractAllPrescriptions(visits: UserVisits): MutableList<PrescriptionUpdate> {
        val allPrescription: MutableList<PrescriptionUpdate> = mutableListOf()
        visits.allVisits.forEach {
            it.prescriptions.forEach { prescription ->
                allPrescription.add(prescription.toPrescriptionUpdate())
            }
        }
        return allPrescription
    }

    override fun insertPrescription(
        visitId: ObjectId,
        prescriptionUpdate: PrescriptionUpdate,
        visits: UserVisits
    ) {
        visits.allVisits.first { it.id == visitId }.prescriptions.addFirst(
            Prescription(
                id = ObjectId.get(),
                visitId = visitId,
                drugName = prescriptionUpdate.drugName,
                dosage = prescriptionUpdate.dosage,
                duration = prescriptionUpdate.duration,
                notes = prescriptionUpdate.notes,
                frequency = prescriptionUpdate.frequency,
                prescribedAt = Instant.now()
            )
        )
    }

    override fun updatePrescriptions(
        prescriptionId: ObjectId,
        visitId: ObjectId,
        prescriptionUpdate: PrescriptionUpdate,
        visits: UserVisits
    ) {
        val existingPrescriptions = visits.allVisits.first { it.id == visitId }.prescriptions
        val indexAt = existingPrescriptions.indexOfFirst { it.id == prescriptionId }
        val newPrescription = existingPrescriptions.first { it.id == prescriptionId }.copy(
            drugName = prescriptionUpdate.drugName,
            dosage = prescriptionUpdate.dosage,
            duration = prescriptionUpdate.duration,
            notes = prescriptionUpdate.notes,
            frequency = prescriptionUpdate.frequency,
        )
        visits.allVisits.first { it.id == visitId }.prescriptions[indexAt] = newPrescription
    }

    override fun removePrescription(
        prescriptionId: ObjectId,
        visitId: ObjectId,
        visits: UserVisits
    ) {
        visits.allVisits.first { it.id == visitId }.prescriptions.removeIf { prescription -> prescriptionId == prescription.id }
    }

    override fun extractAllLabResults(visits: UserVisits): MutableList<LabResultUpdate> {
        val allLabResults: MutableList<LabResultUpdate> = mutableListOf()
        visits.allVisits.forEach { it.labResults.forEach { labResult -> allLabResults.add(labResult.toLabResultUpdate()) } }
        return allLabResults
    }

    override fun insertLabResult(
        labResultUpdate: LabResultUpdate,
        visitId: ObjectId,
        visits: UserVisits
    ) {
        visits.allVisits.first { it.id == visitId }.labResults.addFirst(
            LabResult(
                id = ObjectId.get(),
                visitId = visitId,
                testName = labResultUpdate.testName,
                result = labResultUpdate.result,
                normalRange = labResultUpdate.normalRange,
                date = labResultUpdate.date,
                notes = labResultUpdate.notes,
                testedAt = Instant.now()
            )
        )
    }

    override fun updateLabResult(
        labResultId: ObjectId,
        visitId: ObjectId,
        labResultUpdate: LabResultUpdate,
        visits: UserVisits
    ) {
        val existingResults = visits.allVisits.first { it.id == visitId }.labResults
        val index = existingResults.indexOfFirst { it.id == labResultId }
        val updatedLabResult = existingResults.first { it.id == labResultId }.copy(
            testName = labResultUpdate.testName,
            result = labResultUpdate.result,
            normalRange = labResultUpdate.normalRange,
            date = labResultUpdate.date,
            notes = labResultUpdate.notes,
            testedAt = labResultUpdate.testedAt
        )
        visits.allVisits.first { it.id == visitId }.labResults[index] = updatedLabResult
    }

    override fun removeLabResult(
        labResultId: ObjectId,
        visitId: ObjectId,
        visits: UserVisits
    ) {
        visits.allVisits.first { it.id == visitId }.labResults.removeIf { it.id == labResultId }
    }

    override fun extractAllDiagnosis(visits: UserVisits): MutableList<DiagnosisUpdate> {
        val allDiagnosisUpdates: MutableList<DiagnosisUpdate> = mutableListOf()
        visits.allVisits.forEach { it.diagnosis.forEach { diagnosis -> allDiagnosisUpdates.add(diagnosis.toDiagnosisUpdate()) } }
        return allDiagnosisUpdates
    }

    override fun insertDiagnosis(
        visitId: ObjectId,
        diagnosisUpdate: DiagnosisUpdate,
        visits: UserVisits
    ) {
        visits.allVisits.first { it.id == visitId }.diagnosis.addFirst(
            Diagnosis(
                id = ObjectId.get(),
                visitId = visitId,
                condition = diagnosisUpdate.condition,
                description = diagnosisUpdate.description,
                severity = diagnosisUpdate.severity,
                status = diagnosisUpdate.status,
                codedValue = diagnosisUpdate.codedValue,
                diagnosedAt = diagnosisUpdate.diagnosedAt,
                updatedAt = Instant.now()
            )
        )
    }

    override fun updateDiagnosis(
        diagnosisId: ObjectId,
        visitId: ObjectId,
        update: DiagnosisUpdate,
        visits: UserVisits
    ) {
        val existingDiagnosis = visits.allVisits.first { it.id == visitId }.diagnosis
        val index = existingDiagnosis.indexOfFirst { it.id == diagnosisId }
        val updatedDiagnosis = existingDiagnosis.first { it.id == diagnosisId }.copy(
            condition = update.condition,
            description = update.description,
            severity = update.severity,
            status = update.status,
            codedValue = update.codedValue,
            diagnosedAt = update.diagnosedAt,
            updatedAt = update.updatedAt
        )
        visits.allVisits.first { it.id == visitId }.diagnosis[index] = updatedDiagnosis
    }

    override fun removeDiagnosis(
        diagnosisId: ObjectId,
        visitId: ObjectId,
        visits: UserVisits
    ) {
        visits.allVisits.first { it.id == visitId }.diagnosis.removeIf { it.id == diagnosisId }
    }

    override fun insertVisit(
        newVisit: VisitUpdate,
        userVisits: UserVisits
    ) {
        val newVisitId: ObjectId = ObjectId.get()
        val allDiagnosis: MutableList<Diagnosis> = mutableListOf()
        if (newVisit.diagnosis.isNotEmpty()){
            for (diagnosisUpdate: DiagnosisUpdate in newVisit.diagnosis){
                allDiagnosis.add(
                    Diagnosis(
                        id = ObjectId.get(),
                        visitId = newVisitId,
                        condition = diagnosisUpdate.condition,
                        description = diagnosisUpdate.description,
                        severity = diagnosisUpdate.severity,
                        status = diagnosisUpdate.status,
                        codedValue = diagnosisUpdate.codedValue,
                        diagnosedAt = diagnosisUpdate.diagnosedAt,
                        updatedAt = diagnosisUpdate.updatedAt
                    )
                )
            }
        }
        val allPrescriptions: MutableList<Prescription> = mutableListOf()
        if(newVisit.prescriptions.isNotEmpty()){
            for (prescriptionUpdate: PrescriptionUpdate in newVisit.prescriptions){
                allPrescriptions.add(
                    Prescription(
                        id = ObjectId.get(),
                        visitId = newVisitId,
                        drugName = prescriptionUpdate.drugName,
                        dosage = prescriptionUpdate.dosage,
                        frequency = prescriptionUpdate.frequency,
                        duration = prescriptionUpdate.duration,
                        notes = prescriptionUpdate.notes,
                        prescribedAt = Instant.now()
                    )
                )
            }
        }
        val allLabResults: MutableList<LabResult> = mutableListOf()
        if(newVisit.labResults.isNotEmpty()){
            for (labUpdate: LabResultUpdate in newVisit.labResults){
                allLabResults.add(
                    LabResult(
                        id = ObjectId.get(),
                        visitId = newVisitId,
                        testName = labUpdate.testName,
                        result = labUpdate.result,
                        normalRange = labUpdate.normalRange,
                        date = labUpdate.date,
                        notes = labUpdate.notes,
                        testedAt = labUpdate.testedAt

                    )
                )
            }
        }
        userVisits.allVisits.add(
            Visit(
                id = newVisitId,
                date = newVisit.date,
                department = newVisit.department,
                doctor = newVisit.doctor,
                reasonForVisit = newVisit.reasonForVisit,
                notes = newVisit.notes,
                visitAt = newVisit.visitAt,
                diagnosis = allDiagnosis,
                prescriptions = allPrescriptions,
                labResults = allLabResults
            )
        )
    }

    override fun updateVisit(
        visitId: ObjectId,
        update: UserVisitUpdate,
        userVisits: UserVisits
    ) {
        val updatedCopy = userVisits.allVisits.first { it.id == visitId }.copy(
            date = update.date,
            doctor = update.doctor,
            department = update.department,
            notes = update.notes,
            reasonForVisit = update.reasonForVisit,
            visitAt = update.visitAt,
        )
        userVisits.updatedAt = Instant.now()
        userVisits.allVisits[userVisits.allVisits.indexOfFirst { it.id == visitId }] = updatedCopy
    }

    override fun removeVisit(
        visitId: ObjectId,
        userVisits: UserVisits
    ) {
        userVisits.allVisits.removeIf { it.id == visitId }
    }

}