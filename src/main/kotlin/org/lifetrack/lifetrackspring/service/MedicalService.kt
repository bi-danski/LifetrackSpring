package org.lifetrack.lifetrackspring.service

import com.mongodb.MongoException
import com.mongodb.MongoWriteException
import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.delegate.MedicalDelegate
import org.lifetrack.lifetrackspring.database.model.data.MedicalHistory
import org.lifetrack.lifetrackspring.database.model.dto.MedicalPRequest
import org.lifetrack.lifetrackspring.database.repository.MedicalRepository
import org.lifetrack.lifetrackspring.exception.ResourceNotFound
import org.lifetrack.lifetrackspring.utils.ValidationUtil
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class MedicalService(
    private val medicalRepository: MedicalRepository,
    private val validationUtil: ValidationUtil,
    @param:Qualifier("medicalDelegateImpl") private val medicalDelegate: MedicalDelegate
) : MedicalDelegate by medicalDelegate {

    fun eraseMedicalHistory(userId: ObjectId, accessToken: String): HttpStatus{
        if(!validationUtil.validateRequestFromUser(userId, accessToken)){
            throw AccessDeniedException(HttpStatus.UNAUTHORIZED.toString())
        }
        if(!medicalRepository.existsByOwnerId(userId)) {
            return HttpStatus.NOT_FOUND
        }
        try {
            medicalRepository.deleteMedicalHistoryByOwnerId(userId)
        }catch (_: MongoException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }
        return HttpStatus.OK
    }

    fun createMedicalHistory(userId: ObjectId, medHub: MedicalHistory, accessToken: String): HttpStatus{
        if(!validationUtil.validateRequestFromUser(userId, accessToken)){
            throw AccessDeniedException(HttpStatus.UNAUTHORIZED.toString())
        }
        if(medicalRepository.findById(medHub.id).isPresent){
            return HttpStatus.CONFLICT
        }
        try {
            medicalRepository.save<MedicalHistory>(medHub)
        }catch (_: MongoWriteException){
            return HttpStatus.UNPROCESSABLE_ENTITY
        }catch (_: MongoException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }
        return HttpStatus.CREATED
    }

    @Transactional
    fun amendMedicalHistory(medicalRequest: MedicalPRequest, accessToken: String): HttpStatus {
        if (!validationUtil.validateRequestFromUser(ObjectId(medicalRequest.ownerId), accessToken)) {
            throw AccessDeniedException(HttpStatus.UNAUTHORIZED.toString())
        }
        if(!medicalRepository.existsByOwnerId(ObjectId(medicalRequest.ownerId))){
            return HttpStatus.NOT_FOUND
        }
        val existingEntity = medicalRepository.findMedicalHistoryByOwnerId(ObjectId(medicalRequest.ownerId))
        val updatedEntity = existingEntity.copy(
            allergies = medicalRequest.allergies.ifEmpty { existingEntity.allergies },
            chronicConditions = medicalRequest.chronicConditions.ifEmpty { existingEntity.chronicConditions },
            pastSurgeries = medicalRequest.pastSurgeries.ifEmpty { existingEntity.pastSurgeries },
            familyHistory = medicalRequest.familyHistory.ifEmpty { existingEntity.familyHistory },
//            visits = medicalRequest.visits.ifEmpty { existingEntity.visits },
            updatedAt = java.time.Instant.now()
        )
        return try {
            medicalRepository.save(updatedEntity)
            HttpStatus.OK
        } catch (_: MongoWriteException) {
            HttpStatus.UNPROCESSABLE_ENTITY
        } catch (_: MongoException) {
            HttpStatus.INTERNAL_SERVER_ERROR
        }
    }

    fun retrieveMedicalHistory(userId: ObjectId, accessToken: String): MedicalHistory{
        if(!validationUtil.validateRequestFromUser(userId, accessToken)){
            throw AccessDeniedException(HttpStatus.UNAUTHORIZED.toString())
        }
        if (!medicalRepository.existsByOwnerId(userId)){
            throw ResourceNotFound("Medical History Not Found")
        }
        return medicalRepository.findMedicalHistoryByOwnerId(userId)
    }

}