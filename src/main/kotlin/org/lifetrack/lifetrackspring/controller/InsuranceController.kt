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
    private val insuranceService: InsuranceService,
) {
    final fun userId() = ObjectId(SecurityContextHolder.getContext().authentication.principal as String)

    @GetMapping("/insurance")
    fun getUserInsuranceById(@RequestParam insuranceId: String): InsuranceResponse {
        return insuranceService.retrieveInsurance(ObjectId(insuranceId))
    }

    @GetMapping("/insurance")
    fun getUserInsuranceByUserId(@RequestParam id: String): InsuranceResponse {
        return insuranceService.retrieveInsuranceByUserId(ObjectId(id))
    }

        @DeleteMapping(path = ["/{insuranceId}"])
    fun deleteUserInsurance(@PathVariable insuranceId: String): HttpStatus {
        return insuranceService.eraseInsurance(ObjectId(insuranceId))
    }

    @PostMapping("/insurance")
    fun saveUserInsurance(@RequestBody body: InsuranceRequest): HttpStatus {
        return insuranceService.storeInsurance(userId(), body)

    }

    @PatchMapping("/insurance")
    fun updateUserInsurance(@RequestBody body: InsuranceRequest): HttpStatus {
        return insuranceService.amendInsurance(userId(), body)
    }
}