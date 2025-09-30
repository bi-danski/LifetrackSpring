package org.lifetrack.lifetrackspring.controller

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.dto.BillingRequest
import org.lifetrack.lifetrackspring.database.model.dto.BillingResponse
import org.lifetrack.lifetrackspring.database.model.helpers.toBillingsResponse
import org.lifetrack.lifetrackspring.service.BillingService
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user/billing")
class BillingController(
    private val billingService: BillingService,
) {
    final fun userId() = ObjectId(SecurityContextHolder.getContext().authentication.principal as String)
    @GetMapping
    fun getUserBillings(): BillingResponse {
        return billingService.retrieveBillings(userId()).toBillingsResponse()
    }

    @PatchMapping
    fun updateUserBillings(@RequestBody body: BillingRequest): HttpStatus{
        return billingService.amendBillings(userId(), body)
    }

    @PostMapping
    fun createUserBillings(@RequestBody body: BillingRequest): HttpStatus{
        return billingService.createBillings(userId = userId(), billingRequest = body)
    }

    @DeleteMapping
    fun deleteUserBillings(): HttpStatus{
        return billingService.eraseBillings(userId())
    }

    @DeleteMapping(path=["/{id}"])
    fun deleteUserBillingsInfo(@PathVariable id: String): HttpStatus{
        return billingService.eraseBilling(
            userId(),
            billingId = ObjectId(id),
        )
    }
}