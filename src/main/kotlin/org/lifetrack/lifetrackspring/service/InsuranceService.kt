package org.lifetrack.lifetrackspring.service

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.Insurance
import org.lifetrack.lifetrackspring.database.repository.InsuranceRepository
import org.lifetrack.lifetrackspring.utils.ValidationUtil
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InsuranceService(
    private val insuranceRepository: InsuranceRepository,
    private val validationUtil: ValidationUtil
) {
    fun retrieveInsurance(insuranceId: ObjectId, accessToken: String): Insurance{
        if(!validationUtil.validateRequestFromUser(insuranceId, accessToken)){
            throw AccessDeniedException(HttpStatus.UNAUTHORIZED.toString())
        }
        return insuranceRepository.getInsuranceById(insuranceId)
    }

    fun eraseInsurance(insuranceId: ObjectId, accessToken: String): HttpStatus{
        if(!validationUtil.validateRequestFromUser(insuranceId, accessToken)){
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
        if(!validationUtil.validateRequestFromUser(insurance.id, accessToken)){
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
        if(!validationUtil.validateRequestFromUser(insurance.id, accessToken)){
            return HttpStatus.UNAUTHORIZED
        }
        insuranceRepository.save<Insurance>(insurance)
        return HttpStatus.CREATED
    }
}