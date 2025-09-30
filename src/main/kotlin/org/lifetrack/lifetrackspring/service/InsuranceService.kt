package org.lifetrack.lifetrackspring.service

import com.mongodb.MongoException
import com.mongodb.MongoWriteException
import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.Insurance
import org.lifetrack.lifetrackspring.database.model.dto.InsuranceRequest
import org.lifetrack.lifetrackspring.database.model.dto.InsuranceResponse
import org.lifetrack.lifetrackspring.database.model.helpers.toInsuranceResponse
import org.lifetrack.lifetrackspring.database.repository.InsuranceRepository
import org.lifetrack.lifetrackspring.exception.ResourceNotFound
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class InsuranceService(
    private val insuranceRepository: InsuranceRepository,
) {
    fun retrieveInsurance(insuranceId: ObjectId): InsuranceResponse{
        return insuranceRepository.getInsuranceById(insuranceId).toInsuranceResponse()
    }

    fun retrieveInsuranceByUserId(userId: ObjectId): InsuranceResponse {
        if (!insuranceRepository.existsInsuranceByOwnerId(userId)) {
            throw ResourceNotFound(HttpStatus.NOT_FOUND.toString())
        }
        return insuranceRepository.findInsuranceByOwnerId(userId).toInsuranceResponse()
    }

    fun eraseInsurance(insuranceId: ObjectId): HttpStatus{
        return try {
            insuranceRepository.deleteInsuranceById(insuranceId)
            HttpStatus.OK
        }catch (_: MongoException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }
    }

    @Transactional
    fun amendInsurance(userId: ObjectId, insurance: InsuranceRequest): HttpStatus{
        return try {
            val updatedInsurance = insuranceRepository.findInsuranceByOwnerId(userId).copy(
                provider = insurance.provider,
                coverage = insurance.coverage,
                policyNumber = insurance.policyNumber,
                updatedAt = Instant.now()
            )
            insuranceRepository.save<Insurance>(updatedInsurance)
            HttpStatus.OK
        }catch (_: MongoWriteException){
            HttpStatus.UNPROCESSABLE_ENTITY
        }catch (_: MongoException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }
    }

    fun storeInsurance(userId: ObjectId, insurance: InsuranceRequest): HttpStatus{
        return try {
            if(insuranceRepository.existsInsuranceByOwnerId(userId)){
                return HttpStatus.CONFLICT
            }
            insuranceRepository.save<Insurance>(
                Insurance(
                    id = ObjectId.get(),
                    ownerId = userId,
                    provider = insurance.provider,
                    policyNumber = insurance.policyNumber,
                    coverage = insurance.coverage,
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
}