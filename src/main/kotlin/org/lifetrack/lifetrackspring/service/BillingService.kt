package org.lifetrack.lifetrackspring.service

import com.mongodb.MongoException
import com.mongodb.MongoInternalException
import com.mongodb.MongoQueryException
import com.mongodb.MongoWriteException
import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.delegate.BillingsDelegate
import org.lifetrack.lifetrackspring.database.model.data.Billing
import org.lifetrack.lifetrackspring.database.model.data.Billings
import org.lifetrack.lifetrackspring.database.model.dto.BillingRequest
import org.lifetrack.lifetrackspring.database.repository.BillingRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class BillingService(
    private val billingRepository: BillingRepository,
    @param:Qualifier("billingsDelegateImpl") private val billingsDelegate: BillingsDelegate
) : BillingsDelegate by billingsDelegate {

    fun createBillings(userId: ObjectId, billingRequest: BillingRequest): HttpStatus{
        val newBillings = Billings(
            id = ObjectId.get(),
            ownerId = userId,
            billingInfo = mutableListOf<Billing>(
                Billing(
                    id = ObjectId.get(),
                    visitId = ObjectId(billingRequest.visitId),
                    totalAmount = billingRequest.totalAmount,
                    status = billingRequest.status,
                    service = billingRequest.service,
                    paymentMethod = billingRequest.paymentMethod,
                    transactionId = billingRequest.transactionId,
                    updatedAt = Instant.now(),
                    createdAt = Instant.now(),
                    ownerId = userId
                )
            ),
            updatedAt = Instant.now()
        )
        return try {
            billingRepository.save<Billings>(newBillings)
            HttpStatus.CREATED
        }catch (_: MongoException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }catch(_: MongoWriteException){
            HttpStatus.UNPROCESSABLE_ENTITY
        }
    }

    fun retrieveBillings(userId: ObjectId): Billings{
        return billingRepository.findBillingsByOwnerId(userId)
    }

    fun amendBillings(userId: ObjectId, billingRequest: BillingRequest): HttpStatus{
        return try {
            val billingsResp = billingRepository.findBillingsByOwnerId(userId)
            val index = billingsResp.billingInfo.indexOfFirst { it.visitId == ObjectId(billingRequest.visitId) }
            val updatedBill = billingsResp.billingInfo.first { it.visitId == ObjectId(billingRequest.visitId) }.copy(
                totalAmount = billingRequest.totalAmount,
                status = billingRequest.status,
                service = billingRequest.service,
            )
            if (index != -1) {
                billingsResp.billingInfo[index] = updatedBill
            }else{
                return HttpStatus.NOT_FOUND
            }
            val updatedBillings = billingsResp.copy(
                updatedAt = Instant.now()
            )
            billingRepository.save(updatedBillings)
            HttpStatus.OK
        }catch (_: MongoException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }
        catch(_: Exception){
            HttpStatus.UNPROCESSABLE_ENTITY
        }

    }

    fun eraseBillings(userId: ObjectId): HttpStatus{
        return try {
            if(billingRepository.existsBillingsById(userId)) {
                billingRepository.deleteBillingsByOwnerId(userId)
            }else{
                return HttpStatus.NOT_FOUND
            }
            HttpStatus.OK
        }catch (_: MongoException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }
    }

    @Transactional
    fun eraseBilling(userId: ObjectId, billingId: ObjectId): HttpStatus{
        return try{
            val billResp = billingRepository.findBillingsByOwnerId(userId)
            if(billResp.billingInfo.isEmpty()){
                return HttpStatus.NOT_FOUND
            }
            val finalBillings = removeBillingsInfoById(billingId, billResp)
            billingRepository.save<Billings>(finalBillings)
            HttpStatus.OK
        }catch (_: MongoQueryException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: MongoException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: MongoInternalException){
            HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: Exception){
            HttpStatus.SERVICE_UNAVAILABLE
        }
    }
}