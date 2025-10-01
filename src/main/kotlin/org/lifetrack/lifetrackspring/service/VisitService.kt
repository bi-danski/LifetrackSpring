package org.lifetrack.lifetrackspring.service

import com.mongodb.MongoException
import com.mongodb.MongoQueryException
import com.mongodb.MongoWriteException
import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.delegate.VisitDelegate
import org.lifetrack.lifetrackspring.database.model.data.UserVisits
import org.lifetrack.lifetrackspring.database.model.dto.*
import org.lifetrack.lifetrackspring.database.repository.VisitRepository
import org.lifetrack.lifetrackspring.exception.ResourceNotFound
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class VisitService(
    private val visitRepository: VisitRepository,
    @param:Qualifier("visitDelegateImpl") private val visitDelegate: VisitDelegate
) : VisitDelegate by visitDelegate {

    fun retrieveVisits(userId: ObjectId): UserVisits{
        if(!visitRepository.existsUserVisitsByOwnerId(userId)){
            throw ResourceNotFound(HttpStatus.NOT_FOUND.toString())
        }
        return visitRepository.findUserVisitsByOwnerId(userId)
    }

    fun eraseVisit(visitId: ObjectId, userId: ObjectId): HttpStatus{
        return try {
            val userVisits = retrieveVisits(userId)
            visitDelegate.removeVisit(visitId, userVisits)
            visitRepository.save(userVisits)
            HttpStatus.OK
        }catch (_: MongoQueryException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }
    }

    fun createVisitsDocument(userId: ObjectId, visitData: VisitUpdate): HttpStatus{
           val userVisits = UserVisits(id = ObjectId.get(), ownerId = userId, updatedAt = Instant.now())
        visitDelegate.insertVisit(visitData, userVisits)

        if(visitRepository.findById(userVisits.id).isPresent){
            return HttpStatus.CONFLICT
        }
        return try {
            visitRepository.save(userVisits)
            HttpStatus.OK
        }catch (_: MongoWriteException){
            HttpStatus.UNPROCESSABLE_ENTITY
        }catch (_: MongoException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }
    }

    fun amendVisitInfo(
        visitId: ObjectId, userId: ObjectId,
        userVisitData: UserVisitUpdate
    ): HttpStatus {
        val userVisits = retrieveVisits(userId)
        visitDelegate.updateVisit(visitId,userVisitData, userVisits)
        return try {
            if(!visitRepository.existsById(userVisits.id)){
                return HttpStatus.NOT_FOUND
            }
            visitRepository.save(userVisits)
            HttpStatus.OK
        }catch (_: MongoWriteException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: MongoException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }
    }

    fun amendVisitPrescriptions(userId: ObjectId, userRequest: UserPrescriptionRequest): HttpStatus{
        return try {
            val userVisits = retrieveVisits(userId)
            visitDelegate.insertPrescription(ObjectId(userRequest.visitId),
                userRequest.prescriptionUpdate,
                userVisits )
            visitRepository.save(userVisits)
            HttpStatus.OK
        }catch (_: MongoWriteException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: MongoException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: ResourceNotFound){
            HttpStatus.NOT_FOUND
        }
    }

    fun eraseVisitPrescription(visitId: ObjectId, prescriptionId: ObjectId, userId: ObjectId): HttpStatus{
        return try {
            val visits = retrieveVisits(userId)
            visitDelegate.removePrescription(prescriptionId, visitId, visits)
            visitRepository.save(visits)
            HttpStatus.OK
        }catch (_: MongoException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: MongoWriteException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: ResourceNotFound){
            HttpStatus.NOT_FOUND
        }
    }

    fun amendVisitLabResults(userId: ObjectId, userRequest: UserLabRequest): HttpStatus {
        val userVisits = retrieveVisits(userId)
        return try {
            visitDelegate.insertLabResult(labResultUpdate =  userRequest.labResultUpdate,
                visitId = ObjectId(userRequest.visitId),
                visits = userVisits
            )
            visitRepository.save(userVisits)
            HttpStatus.OK
        }catch (_: MongoWriteException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: MongoException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: ResourceNotFound){
            HttpStatus.NOT_FOUND
        }
    }

    fun eraseVisitLabResults(visitId: ObjectId, resultId: ObjectId, userId: ObjectId): HttpStatus{
        return try{
            val visits = retrieveVisits(userId)
            visitDelegate.removeLabResult(resultId, visitId, visits)
            visitRepository.save(visits)
            HttpStatus.OK
        }catch (_: MongoWriteException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: MongoException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: ResourceNotFound){
            HttpStatus.NOT_FOUND
        }
    }

    fun amendVisitDiagnosis(userId: ObjectId, userRequest: UserDiagnosisRequest): HttpStatus{
        return try {
            val userVisits = retrieveVisits(userId)
            visitDelegate.insertDiagnosis(userVisits.id,
                diagnosisUpdate = userRequest.diagnosisUpdate,
                visits = userVisits
            )
            visitRepository.save(userVisits)
            HttpStatus.OK
        }catch (_: MongoWriteException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: MongoException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: ResourceNotFound){
            HttpStatus.NOT_FOUND
        }
    }

    fun eraseVisitDiagnosis(visitId: ObjectId, diagnosisId: ObjectId, userId: ObjectId): HttpStatus{
        return try{
            val visits = retrieveVisits(userId)
            visitDelegate.removeDiagnosis(diagnosisId,
                visitId = visitId,
                visits = visits
            )
            visitRepository.save(visits)
            HttpStatus.OK
        }catch (_: MongoException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: MongoWriteException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: ResourceNotFound){
            HttpStatus.NOT_FOUND
        }
    }
}