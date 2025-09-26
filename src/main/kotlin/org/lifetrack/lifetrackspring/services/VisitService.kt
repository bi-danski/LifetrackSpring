package org.lifetrack.lifetrackspring.services

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.delegate.VisitDelegate
import org.lifetrack.lifetrackspring.database.model.dto.VisitUpdate
import org.lifetrack.lifetrackspring.database.repository.VisitRepository
import org.lifetrack.lifetrackspring.utils.ValidationUtil
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class VisitService(
    private val visitRepository: VisitRepository,
    private val validationUtil: ValidationUtil,
    @param:Qualifier("visitDelegateImpl") private val visitDelegate: VisitDelegate
) : VisitDelegate by visitDelegate {

    fun retrieveVisits(userId: ObjectId, accessToken: String){

    }

    fun eraseVisits(userId: ObjectId, accessToken: String){

    }

    fun amendVisits(userId: ObjectId, accessToken: String, visitUpdate: VisitUpdate){

    }

    fun createVisit(userId: ObjectId, accessToken: String, visitData: VisitUpdate): HttpStatus{

        if(!validationUtil.validateRequestFromUser(userId, accessToken)){
            return HttpStatus.UNAUTHORIZED
        }
        if(visitRepository.existsVisitsByOwnerId(userId)){
            return HttpStatus.CONFLICT
        }
//        val newVisit = Visit(ownerId = userId, date = LocalDate.now(),department = visitData.department,
//            doctor = visitData.doctor,
//            reasonForVisit = visitData.reasonForVisit,
//            notes = visitData.notes,
//            visitAt = visitData.visitAt,
//            diagnosis = visitData.diagnosis,
//            prescriptions = ,
//            labResults = TODO(),
//            id = ObjectId.get()
//        )
    return HttpStatus.SERVICE_UNAVAILABLE
    }

}