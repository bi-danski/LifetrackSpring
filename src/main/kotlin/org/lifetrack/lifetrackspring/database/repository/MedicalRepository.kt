package org.lifetrack.lifetrackspring.database.repository

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.MedicalHistory
import org.springframework.data.mongodb.repository.MongoRepository

interface MedicalRepository: MongoRepository<MedicalHistory, ObjectId> {
    fun findMedicalHistoryByOwnerId(ownerId: ObjectId): MedicalHistory
    fun deleteMedicalHistoryByOwnerId(ownerId: ObjectId)
}