package org.lifetrack.lifetrackspring.services

import com.mongodb.MongoException
import com.mongodb.MongoQueryException
import com.mongodb.MongoWriteException
import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.delegate.VisitDelegate
import org.lifetrack.lifetrackspring.database.model.data.UserVisits
import org.lifetrack.lifetrackspring.database.model.dto.*
import org.lifetrack.lifetrackspring.database.repository.VisitRepository
import org.lifetrack.lifetrackspring.exception.ResourceNotFound
import org.lifetrack.lifetrackspring.utils.ValidationUtil
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class VisitService(
    private val visitRepository: VisitRepository,
    private val validationUtil: ValidationUtil,
    @param:Qualifier("visitDelegateImpl") private val visitDelegate: VisitDelegate
) : VisitDelegate by visitDelegate {

    fun retrieveVisits(userId: ObjectId, accessToken: String): UserVisits{
        if(!validationUtil.validateRequestFromUser(userId, accessToken)){
            throw AccessDeniedException(HttpStatus.UNAUTHORIZED.toString())
        }
        if(!visitRepository.existsUserVisitsByOwnerId(userId)){
            throw ResourceNotFound(HttpStatus.NOT_FOUND.toString())
        }
        return visitRepository.findUserVisitsByOwnerId(userId)
    }

    fun eraseVisit(visitId: ObjectId, userId: ObjectId, accessToken: String ): HttpStatus{
        if (!validationUtil.validateRequestFromUser(userId, accessToken)){
            return HttpStatus.UNAUTHORIZED
        }
        return try {
            val userVisits = retrieveVisits(userId, accessToken)
            visitDelegate.removeVisit(visitId, userVisits)
            visitRepository.save(userVisits)
            HttpStatus.OK
        }catch (_: MongoQueryException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }
    }

    fun createVisitsDocument(userId: ObjectId, accessToken: String, visitData: VisitUpdate): HttpStatus{
        if(!validationUtil.validateRequestFromUser(userId, accessToken)){
            return HttpStatus.UNAUTHORIZED
        }
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

    fun amendVisitInfo(visitId: ObjectId, userId: ObjectId, accessToken: String, userVisitData: UserVisitUpdate): HttpStatus{
        if(!validationUtil.validateRequestFromUser(userId, accessToken)){
            return HttpStatus.UNAUTHORIZED
        }
        val userVisits = retrieveVisits(userId, accessToken)
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

    fun amendVisitPrescriptions(userRequest: UserPrescriptionRequest): HttpStatus{
        if(!validationUtil.validateRequestFromUser(ObjectId(userRequest.userId), userRequest.accessToken)){
            return HttpStatus.UNAUTHORIZED
        }
        val userVisits = retrieveVisits(ObjectId(userRequest.userId),
            userRequest.accessToken
        )
        return try {
            visitDelegate.insertPrescription(ObjectId(userRequest.visitId),
                userRequest.prescriptionUpdate,
                userVisits )
            visitRepository.save(userVisits)
            HttpStatus.OK
        }catch (_: MongoWriteException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: MongoException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }
    }

    fun eraseVisitPrescription(visitId: ObjectId, prescriptionId: ObjectId, userId: ObjectId, accessToken: String): HttpStatus{
        if (!validationUtil.validateRequestFromUser(userId, accessToken)){
            return HttpStatus.UNAUTHORIZED
        }
        return try {
            val visits = retrieveVisits(userId, accessToken)
            visitDelegate.removePrescription(prescriptionId, visitId, visits)
            visitRepository.save(visits)
            HttpStatus.OK
        }catch (_: MongoException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: MongoWriteException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }
    }

    fun amendVisitLabResults(userRequest: UserLabRequest): HttpStatus {
        if (!validationUtil.validateRequestFromUser(ObjectId(userRequest.userId), userRequest.accessToken)) {
            return HttpStatus.UNAUTHORIZED
        }
        val userVisits = retrieveVisits(ObjectId(userRequest.userId), userRequest.accessToken)
        return try {
            visitDelegate.insertLabResult(labResultUpdate =  userRequest.labResultUpdate,
                visitId = ObjectId(userRequest.visitId),
                userVisits
            )
            visitRepository.save(userVisits)
            HttpStatus.OK
        }catch (_: MongoWriteException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: MongoException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }
    }

    fun eraseVisitLabResults(visitId: ObjectId, resultId: ObjectId, userId: ObjectId, accessToken: String): HttpStatus{
        if(!validationUtil.validateRequestFromUser(userId, accessToken)){
            return HttpStatus.UNAUTHORIZED
        }
        return try{
            val visits = retrieveVisits(userId, accessToken)
            visitDelegate.removeLabResult(resultId, visitId, visits)
            visitRepository.save(visits)
            HttpStatus.OK
        }catch (_: MongoWriteException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: MongoException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }
    }

    fun amendVisitDiagnosis(userRequest: UserDiagnosisRequest): HttpStatus{
        if (!validationUtil.validateRequestFromUser(ObjectId(userRequest.userId), userRequest.accessToken)) {
            return HttpStatus.UNAUTHORIZED
        }
        val userVisits = retrieveVisits(ObjectId(userRequest.userId), userRequest.accessToken)
        return try {
            visitDelegate.insertDiagnosis(userVisits.id,
                diagnosisUpdate = userRequest.diagnosisUpdate,
                userVisits
            )
            visitRepository.save(userVisits)
            HttpStatus.OK
        }catch (_: MongoWriteException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: MongoException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }
    }

    fun eraseVisitDiagnosis(visitId: ObjectId, diagnosisId: ObjectId, userId: ObjectId, accessToken: String): HttpStatus{
        if(!validationUtil.validateRequestFromUser(userId, accessToken)){
            return HttpStatus.UNAUTHORIZED
        }
        return try{
            val visits = retrieveVisits(userId, accessToken)
            visitDelegate.removeDiagnosis(diagnosisId, visitId = visitId, visits)
            visitRepository.save(visits)
            HttpStatus.OK
        }catch (_: MongoException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: MongoWriteException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }
    }
}