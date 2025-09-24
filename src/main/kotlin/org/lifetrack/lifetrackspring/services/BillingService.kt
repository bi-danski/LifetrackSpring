package org.lifetrack.lifetrackspring.services

import com.mongodb.MongoException
import org.bson.types.ObjectId
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

    fun storeBillings(billingsData: Billings, accessToken: String){

    }
    @Transactional
    fun amendBillings(userId: ObjectId, billingsData: Billings, accessToken: String): HttpStatus{
        if(!validationUtil.validateRequestFromUser(userId, accessToken)){
            return HttpStatus.UNAUTHORIZED
        }
        if(billingsData.ownerId != userId){
            return HttpStatus.UNAUTHORIZED
        }
        try {
            billingRepository.findBillingsByOwnerId(userId)
            billingRepository.findBillingsByOwnerId(userId)
            billingRepository.save<Billings>(billingsData)
        }catch (_: MongoException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }
        catch(_: Exception){
            return HttpStatus.CONFLICT
        }
        return HttpStatus.OK
    }
}