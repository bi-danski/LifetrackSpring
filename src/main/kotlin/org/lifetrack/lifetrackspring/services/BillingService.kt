package org.lifetrack.lifetrackspring.services

import com.mongodb.MongoException
import com.mongodb.MongoInternalException
import com.mongodb.MongoQueryException
import com.mongodb.MongoWriteException
import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.Billing
import org.lifetrack.lifetrackspring.database.model.data.Billings
import org.lifetrack.lifetrackspring.database.model.delegate.BillingsDelegate
import org.lifetrack.lifetrackspring.database.repository.BillingRepository
import org.lifetrack.lifetrackspring.utils.ValidationUtil
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BillingService(
    private val billingRepository: BillingRepository,
    private val validationUtil: ValidationUtil
) {
    fun createBillings(userId: ObjectId, billings: Billings, accessToken: String): HttpStatus{
        if(!validationUtil.validateRequestFromUser(userId, accessToken)){
            return HttpStatus.UNAUTHORIZED
        }
        if(billings.ownerId != userId){
            return HttpStatus.UNAUTHORIZED
        }
        try {
            billingRepository.save<Billings>(billings)
        }catch (_: MongoException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }catch(_: MongoWriteException){
            return HttpStatus.UNPROCESSABLE_ENTITY
        }
        return HttpStatus.CREATED
    }

    fun retrieveBillings(userId: ObjectId, accessToken: String): Billings{
        if(!validationUtil.validateRequestFromUser(userId, accessToken)){
            throw AccessDeniedException(HttpStatus.UNAUTHORIZED.toString())
        }
        return billingRepository.findBillingsByOwnerId(userId)
    }

    @Transactional
    fun amendBillings(userId: ObjectId, billingData: Billing, accessToken: String): HttpStatus{
        if(!validationUtil.validateRequestFromUser(userId, accessToken)){
            return HttpStatus.UNAUTHORIZED
        }
        if(billingData.ownerId != userId){
            return HttpStatus.UNAUTHORIZED
        }
        try {
            val billingsResp = billingRepository.findBillingsByOwnerId(userId)
            if(billingsResp.billingInfo.add(billingData)){
                billingRepository.deleteBillingsById(billingsResp.id)
                billingRepository.save<Billings>(billingsResp)
            }
        }catch (_: MongoException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }
        catch(_: Exception){
            return HttpStatus.UNPROCESSABLE_ENTITY
        }
        return HttpStatus.OK
    }

    fun eraseBillings(userId: ObjectId, billingsId: ObjectId, accessToken: String): HttpStatus{
        if(!validationUtil.validateRequestFromUser(userId, accessToken)){
            return HttpStatus.UNAUTHORIZED
        }
        if(billingRepository.findBillingsById(billingsId).id.toString().isEmpty()){
            return HttpStatus.NOT_FOUND
        }
        try {
            billingRepository.deleteBillingsByOwnerId(userId)
        }catch (_: MongoException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }
        return HttpStatus.OK
    }

    @Transactional
    fun eraseBillingsInfo(userId: ObjectId, billingId: ObjectId, accessToken: String): HttpStatus{
        if(!validationUtil.validateRequestFromUser(userId, accessToken)){
            return HttpStatus.UNAUTHORIZED
        }
        val billResp = billingRepository.findBillingsByOwnerId(userId)
        if(billResp.billingInfo.isEmpty()){
            return HttpStatus.NOT_FOUND
        }
        try{
            val finalBillings = BillingsDelegate.removeBillingsInfoById(billingId, billResp)
            billingRepository.deleteBillingsByOwnerId(billResp.id)
            billingRepository.save<Billings>(finalBillings)
        }catch (_: MongoQueryException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: MongoException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: MongoInternalException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: Exception){
            return HttpStatus.SERVICE_UNAVAILABLE
        }
        return HttpStatus.OK
    }
}