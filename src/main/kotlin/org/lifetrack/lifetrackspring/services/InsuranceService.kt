package org.lifetrack.lifetrackspring.services

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.Insurance
import org.lifetrack.lifetrackspring.database.repository.InsuranceRepository
import org.lifetrack.lifetrackspring.utils.Utilities
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InsuranceService(
    private val insuranceRepository: InsuranceRepository,
    private val utilities: Utilities
) {
    fun retrieveInsurance(insuranceId: ObjectId, accessToken: String): Insurance{
        if(!utilities.validateRequestFromUser(insuranceId, accessToken)){
            throw AccessDeniedException(HttpStatus.UNAUTHORIZED.toString())
        }
        return insuranceRepository.getInsuranceById(insuranceId)
    }

    fun eraseInsurance(insuranceId: ObjectId, accessToken: String): HttpStatus{
        if(!utilities.validateRequestFromUser(insuranceId, accessToken)){
            return HttpStatus.UNAUTHORIZED
        }
        if(insuranceRepository.findById(insuranceId).isEmpty){
            return HttpStatus.NOT_FOUND
        }
        insuranceRepository.deleteInsuranceById(insuranceId)
        return HttpStatus.OK
    }

    @Transactional
    fun amendInsurance(insurance: Insurance, accessToken: String): HttpStatus{
        if(!utilities.validateRequestFromUser(insurance.id, accessToken)){
            return HttpStatus.UNAUTHORIZED
        }
        if(insuranceRepository.findById(insurance.id).isEmpty){
            return HttpStatus.NOT_FOUND
        }
        insuranceRepository.deleteInsuranceById(insurance.id)
        insuranceRepository.save<Insurance>(insurance)
        return HttpStatus.OK
    }

    fun storeInsurance(insurance: Insurance, accessToken: String): HttpStatus{
        if(!insuranceRepository.findById(insurance.id).isEmpty){
            return HttpStatus.CONFLICT
        }
        if(!utilities.validateRequestFromUser(insurance.id, accessToken)){
            return HttpStatus.UNAUTHORIZED
        }
        insuranceRepository.save<Insurance>(insurance)
        return HttpStatus.CREATED
    }
}