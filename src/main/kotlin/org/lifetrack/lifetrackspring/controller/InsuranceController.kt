package org.lifetrack.lifetrackspring.controller

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.dto.InsuranceRequest
import org.lifetrack.lifetrackspring.database.model.dto.InsuranceResponse
import org.lifetrack.lifetrackspring.service.InsuranceService
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user/")
class InsuranceController(
    private val insuranceService: InsuranceService
) {
    private val userId = ObjectId(SecurityContextHolder.getContext().authentication.principal as String)

    @GetMapping("/insure")
    fun getUserInsurance(@RequestParam id: String): InsuranceResponse {
        return insuranceService.retrieveInsurance(ObjectId(id))
    }

    @DeleteMapping(path = ["/{insuranceId}"])
    fun deleteUserInsurance(@PathVariable insuranceId: String): HttpStatus {
        return insuranceService.eraseInsurance(ObjectId(insuranceId))
    }

    @PostMapping("/insure")
    fun saveUserInsurance(@RequestBody body: InsuranceRequest): HttpStatus {
        return insuranceService.storeInsurance(userId, body)

    }

    @PatchMapping("/insure")
    fun updateUserInsurance(@RequestBody body: InsuranceRequest): HttpStatus {
        return insuranceService.amendInsurance(userId, body)
    }
}