package org.lifetrack.lifetrackspring.controller

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.*
import org.lifetrack.lifetrackspring.services.BillingService
import org.lifetrack.lifetrackspring.utils.toBillingsResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/user/billing")
class BillingController(
    private val billingService: BillingService,
) {
    @GetMapping
    fun getUserBillings(@RequestBody body: BillingRequest): BillingResponse{
        if (body.accessToken.isEmpty() && body.userId.isEmpty()){
            throw IllegalArgumentException(HttpStatus.BAD_REQUEST.toString())
        }
        return billingService.retrieveBillings(ObjectId(body.userId), body.accessToken).toBillingsResponse()
    }

    @PatchMapping
    fun updateUserBillings(@RequestBody body: BillingPRequest): HttpStatus{
        if (body.accessToken.isEmpty() && body.userId.isEmpty()){
            return HttpStatus.BAD_REQUEST
        }
        if(body.data.id.toString().isEmpty() && body.data.ownerId.toString().isEmpty()){
            return HttpStatus.BAD_REQUEST
        }
        return billingService.amendBillings(ObjectId(body.userId), body.data, body.accessToken)
    }

    @PostMapping
    fun createUserBillings(@RequestBody body: BillingPRequest): HttpStatus{
        if (body.accessToken.isEmpty() && body.userId.isEmpty()){
            return HttpStatus.BAD_REQUEST
        }
        if (body.data.ownerId.toString().isEmpty()){
            return HttpStatus.BAD_REQUEST
        }
        val nwBillingInfo = mutableListOf(Billing(id = body.data.id, visitRefId = body.data.visitRefId, total = body.data.total, status = body.data.status,
            services = body.data.services, paymentMethod = body.data.paymentMethod, transactionId = body.data.transactionId, updatedAt = Instant.now(),
            ownerId = ObjectId(body.userId)))

        return billingService.createBillings(
            userId = ObjectId(body.userId),
            billings = Billings(
                ObjectId.get(),
                ownerId = ObjectId(body.userId),
                billingInfo = nwBillingInfo
            ),
            accessToken = body.accessToken
        )
    }

    @DeleteMapping(path=["/{id}"])
    fun deleteUserBillings(@PathVariable id: String, @RequestBody body: BillingRequest): HttpStatus{
        if (body.accessToken.isEmpty() && body.userId.isEmpty()){
            return HttpStatus.BAD_REQUEST
        }
        return billingService.eraseBillings(ObjectId(body.userId), ObjectId(id), body.accessToken )
    }

    @DeleteMapping(path=["/info/{id}"])
    fun deleteUserBillingsInfo(@PathVariable id: String, @RequestBody body: BillingRequest): HttpStatus{
        if (body.accessToken.isEmpty() && body.userId.isEmpty()){
            return HttpStatus.BAD_REQUEST
        }
        return billingService.eraseBillingsInfo(
            ObjectId(body.userId),
            billingId = ObjectId(id),
            accessToken = body.accessToken
        )
    }

}