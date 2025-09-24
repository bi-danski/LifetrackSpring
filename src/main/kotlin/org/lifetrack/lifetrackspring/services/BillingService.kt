package org.lifetrack.lifetrackspring.services

import com.mongodb.MongoException
import com.mongodb.MongoWriteException
import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.Billing
import org.lifetrack.lifetrackspring.database.model.data.Billings
import org.lifetrack.lifetrackspring.database.repository.BillingRepository
import org.lifetrack.lifetrackspring.utils.ValidationUtil
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BillingService(
    private val billingRepository: BillingRepository,
    private val validationUtil: ValidationUtil
) {
    fun retrieveBillings(billingsId: ObjectId, accessToken: String){

    }

    fun eraseBillings(billingsId: ObjectId, accessToken: String){

    }

    fun eraseBillingsInfo(){

    }

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
            return HttpStatus.CONFLICT
        }
        return HttpStatus.CREATED
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
            return HttpStatus.SERVICE_UNAVAILABLE
        }
        return HttpStatus.OK
    }
}