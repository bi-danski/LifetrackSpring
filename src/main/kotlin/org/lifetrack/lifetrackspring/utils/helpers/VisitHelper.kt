package org.lifetrack.lifetrackspring.utils.helpers

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.Prescription
import org.lifetrack.lifetrackspring.database.model.data.Visit
import org.lifetrack.lifetrackspring.database.model.dto.PrescriptionUpdate
import org.lifetrack.lifetrackspring.exception.ResourceNotFound
import java.time.Instant

fun Visit.addNewPrescription(
    prescriptionUpdate: PrescriptionUpdate
) {
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
        throw ResourceNotFound("Prescription Not Found")
    }
}

fun Visit.updatePrescription(prescriptionId: ObjectId, prescriptionUpdate: PrescriptionUpdate) {
    val index = this.prescriptions.indexOfFirst { it.id == prescriptionId }
    if (index == -1) {
        throw ResourceNotFound("Prescription Not Found")
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