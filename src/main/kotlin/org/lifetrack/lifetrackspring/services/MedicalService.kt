package org.lifetrack.lifetrackspring.services

import com.mongodb.MongoException
import com.mongodb.MongoWriteException
import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.MedicalHistory
import org.lifetrack.lifetrackspring.database.model.delegate.MedicalDelegate
import org.lifetrack.lifetrackspring.database.repository.MedicalRepository
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
    fun amendMedicalHistory(userId: ObjectId, medHub: MedicalHistory, accessToken: String): HttpStatus{
        if(!validationUtil.validateRequestFromUser(userId, accessToken)){
            throw AccessDeniedException(HttpStatus.UNAUTHORIZED.toString())
        }
        val response = medicalRepository.findMedicalHistoryByOwnerId(userId)
        if(response.id.toString().isEmpty()){
            return HttpStatus.NOT_FOUND
        }
        try {
            medicalRepository.deleteMedicalHistoryByOwnerId(userId)
            medicalRepository.save<MedicalHistory>(medHub)
        }catch (_: MongoException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: MongoWriteException){
            return HttpStatus.UNPROCESSABLE_ENTITY
        }
        return HttpStatus.OK
    }

    fun retrieveMedicalHistory(userId: ObjectId, accessToken: String): MedicalHistory{
        if(!validationUtil.validateRequestFromUser(userId, accessToken)){
            throw AccessDeniedException(HttpStatus.UNAUTHORIZED.toString())
        }
        return medicalRepository.findMedicalHistoryByOwnerId(userId)
    }
}