package org.lifetrack.lifetrackspring.controller

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.BillingRequest
import org.lifetrack.lifetrackspring.database.model.data.BillingResponse
import org.lifetrack.lifetrackspring.database.model.data.UserBillingInfo
import org.lifetrack.lifetrackspring.services.BillingService
import org.lifetrack.lifetrackspring.utils.toBillingsResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user/billing")
class BillingController(
    private val billingService: BillingService,
) {
    @GetMapping()
    fun getUserBillings(@RequestBody body: BillingRequest): BillingResponse{
        if (body.accessToken.isEmpty() && body.userId.isEmpty()){
            throw IllegalArgumentException(HttpStatus.BAD_REQUEST.toString())
        }
        return billingService.retrieveBillings(ObjectId(body.userId), body.accessToken).toBillingsResponse()
    }

    @PostMapping()
    fun saveUserBilling(@RequestBody body: BillingRequest, @RequestBody userInfoBody: UserBillingInfo): HttpStatus{
        if (body.accessToken.isEmpty() && body.userId.isEmpty()){
            return HttpStatus.BAD_REQUEST
        }
        if(userInfoBody.billingData.id.toString().isEmpty() && userInfoBody.billingData.ownerId.toString().isEmpty()){
            return HttpStatus.BAD_REQUEST
        }
        return billingService.amendBillings(ObjectId(body.userId), userInfoBody.billingData, body.accessToken)
    }

}