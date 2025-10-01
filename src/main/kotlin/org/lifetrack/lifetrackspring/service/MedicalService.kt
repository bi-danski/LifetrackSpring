package org.lifetrack.lifetrackspring.service

import com.mongodb.MongoException
import com.mongodb.MongoWriteException
import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.delegate.MedicalDelegate
import org.lifetrack.lifetrackspring.database.model.data.MedicalHistory
import org.lifetrack.lifetrackspring.database.model.dto.MedicalPRequest
import org.lifetrack.lifetrackspring.database.repository.MedicalRepository
import org.lifetrack.lifetrackspring.exception.ResourceNotFound
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant


@Service
class MedicalService(
    private val medicalRepository: MedicalRepository,
    @param:Qualifier("medicalDelegateImpl") private val medicalDelegate: MedicalDelegate
) : MedicalDelegate by medicalDelegate {

    fun eraseMedicalHistory(userId: ObjectId): HttpStatus{
        if(!medicalRepository.existsByOwnerId(userId)) {
            return HttpStatus.NOT_FOUND
        }
        return try {
            medicalRepository.deleteMedicalHistoryByOwnerId(userId)
            HttpStatus.OK
        }catch (_: MongoException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }
    }

    fun createMedicalHistory(userId: ObjectId, body: MedicalPRequest): HttpStatus{
        if(medicalRepository.existsByOwnerId(userId)){
            return HttpStatus.CONFLICT
        }
        return try {
            medicalRepository.save<MedicalHistory>(
                MedicalHistory(
                    ownerId = userId,
                    id = ObjectId.get(),
                    allergies = body.allergies,
                    chronicConditions = body.chronicConditions,
                    pastSurgeries = body.pastSurgeries,
                    familyHistory = body.familyHistory,
                    updatedAt = Instant.now(),
                    createdAt = Instant.now()
                )
            )
            HttpStatus.CREATED
        }catch (_: MongoWriteException){
            HttpStatus.UNPROCESSABLE_ENTITY
        }catch (_: MongoException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }
    }

    @Transactional
    fun amendMedicalHistory( userId: ObjectId, medicalRequest: MedicalPRequest): HttpStatus {
         if(!medicalRepository.existsByOwnerId(userId)){
            return HttpStatus.NOT_FOUND
        }
        return try {
            val existingEntity = medicalRepository.findMedicalHistoryByOwnerId(userId)
            val updatedEntity = existingEntity.copy(
                allergies = medicalRequest.allergies.ifEmpty { existingEntity.allergies },
                chronicConditions = medicalRequest.chronicConditions.ifEmpty { existingEntity.chronicConditions },
                pastSurgeries = medicalRequest.pastSurgeries.ifEmpty { existingEntity.pastSurgeries },
                familyHistory = medicalRequest.familyHistory.ifEmpty { existingEntity.familyHistory },
                updatedAt = Instant.now()
            )
            medicalRepository.save(updatedEntity)
            HttpStatus.OK
        } catch (_: MongoWriteException) {
            HttpStatus.UNPROCESSABLE_ENTITY
        } catch (_: MongoException) {
            HttpStatus.INTERNAL_SERVER_ERROR
        }
    }

    fun retrieveMedicalHistory(userId: ObjectId): MedicalHistory{
        if (!medicalRepository.existsByOwnerId(userId)){
            throw ResourceNotFound("Medical History Not Found")
        }
        return medicalRepository.findMedicalHistoryByOwnerId(userId)
    }

}