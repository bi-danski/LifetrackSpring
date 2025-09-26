package org.lifetrack.lifetrackspring.database.model.helpers

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.Diagnosis
import org.lifetrack.lifetrackspring.database.model.data.LabResult
import org.lifetrack.lifetrackspring.database.model.data.Prescription
import org.lifetrack.lifetrackspring.database.model.data.Visit
import org.lifetrack.lifetrackspring.database.model.dto.DiagnosisUpdate
import org.lifetrack.lifetrackspring.database.model.dto.LabResultUpdate
import org.lifetrack.lifetrackspring.database.model.dto.PrescriptionUpdate
import java.time.Instant

fun Visit.addNewPrescription(prescriptionUpdate: PrescriptionUpdate) {
    this.prescriptions.add(
        Prescription(
            id = ObjectId.get(),
            visitId = this.id,
            drugName = prescriptionUpdate.drugName,
            dosage = prescriptionUpdate.dosage,
            frequency = prescriptionUpdate.frequency,
            duration = prescriptionUpdate.duration,
            notes = prescriptionUpdate.notes,
            prescribedAt = Instant.now(),
        )
    )
}
fun Visit.removePrescription(prescriptionId: ObjectId) {
    val removed = this.prescriptions.removeIf { it.id == prescriptionId }
    if (!removed) {
        throw NoSuchElementException("Prescription Not Found")
    }
}
fun Visit.updatePrescription(prescriptionId: ObjectId, prescriptionUpdate: PrescriptionUpdate) {
    val index = this.prescriptions.indexOfFirst { it.id == prescriptionId }
    if (index == -1) {
        throw NoSuchElementException("Prescription Not Found")
    }
    val updatedPrescription = this.prescriptions[index].copy(
        drugName = prescriptionUpdate.drugName,
        dosage = prescriptionUpdate.dosage,
        frequency = prescriptionUpdate.frequency,
        duration = prescriptionUpdate.duration,
        notes = prescriptionUpdate.notes
    )
    this.prescriptions[index] = updatedPrescription
}

fun Visit.addNewLabResult(update: LabResultUpdate) {
    this.labResults.add(
        LabResult(
            id = ObjectId.get(),
            visitId = this.id,
            testName = update.testName,
            result = update.result,
            normalRange = update.normalRange,
            date = update.date,
            notes = update.notes,
            testedAt = Instant.now()
        )
    )
}
fun Visit.removeLabResult(labResultId: ObjectId) {
    val removed = this.labResults.removeIf { it.id == labResultId }
    if (!removed) {
        throw NoSuchElementException("Lab Result Not Found")
    }
}
fun Visit.updateLabResult(labResultId: ObjectId, update: LabResultUpdate) {
    val index = this.labResults.indexOfFirst { it.id == labResultId }
    if (index == -1) {
        throw NoSuchElementException("Lab Result Not Found")
    }
    val updated = this.labResults[index].copy(
        testName = update.testName,
        result = update.result,
        normalRange = update.normalRange,
        date = update.date,
        notes = update.notes
    )
    this.labResults[index] = updated
}

fun Visit.addNewDiagnosis(update: DiagnosisUpdate) {
    this.diagnosis.add(
        Diagnosis(
            id = ObjectId.get(),
            visitId = this.id,
            condition = update.condition,
            description = update.description,
            severity = update.severity,
            status = update.status,
            codedValue = update.codedValue,
            diagnosedAt = Instant.now(),
            updatedAt = Instant.now()
        )
    )
}
fun Visit.removeDiagnosis(diagnosisId: ObjectId) {
    val removed = this.diagnosis.removeIf { it.id == diagnosisId }
    if (!removed) {
        throw NoSuchElementException("Diagnosis Not Found")
    }
}
fun Visit.updateDiagnosis(diagnosisId: ObjectId, update: DiagnosisUpdate) {
    val index = this.diagnosis.indexOfFirst { it.id == diagnosisId }
    if (index == -1) {
        throw NoSuchElementException("Diagnosis Not Found")
    }
    val existing = this.diagnosis[index]
    val updated = existing.copy(
        condition = update.condition,
        description = update.description,
        severity = update.severity,
        status = update.status,
        codedValue = update.codedValue,
        updatedAt = Instant.now()
    )
    this.diagnosis[index] = updated
}

