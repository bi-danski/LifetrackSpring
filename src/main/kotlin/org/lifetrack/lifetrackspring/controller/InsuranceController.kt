package org.lifetrack.lifetrackspring.controller

import org.apache.coyote.BadRequestException
import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.Insurance
import org.lifetrack.lifetrackspring.database.model.dto.InsuranceRequest
import org.lifetrack.lifetrackspring.services.InsuranceService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/user/")
class InsuranceController(
    private val insuranceService: InsuranceService
) {
    @GetMapping("/insure")
    fun getUserInsurance(@RequestParam id: String, @RequestParam accessToken: String): Insurance{
        if (id.isEmpty() && accessToken.isEmpty()){
            throw BadRequestException(HttpStatus.BAD_REQUEST.toString())
        }
        return insuranceService.retrieveInsurance(ObjectId(id), accessToken)
    }
    
    @DeleteMapping(path=["/{insuranceId}"])
    fun deleteUserInsurance(@PathVariable insuranceId: String, @RequestBody insBody: InsuranceRequest): HttpStatus{
        if (insBody.accessToken.isEmpty()){
            return HttpStatus.BAD_REQUEST
        }
        insuranceService.eraseInsurance(ObjectId(insuranceId), accessToken = insBody.accessToken)
        return HttpStatus.OK
    }

    @PostMapping("/insure")
    fun saveUserInsurance(@RequestBody body: InsuranceRequest): HttpStatus{
        if(body.accessToken.isEmpty() && body.ownerId.isEmpty()){
            return HttpStatus.BAD_REQUEST
        }
        return insuranceService.storeInsurance(
            Insurance(
                updatedAt = Instant.now(),
                id = ObjectId(body.ownerId),
                provider = body.provider,
                coverage = body.coverage,
                policyNumber = body.policyNumber
            ),
            accessToken = body.accessToken
        )
    }
    @PatchMapping("/insure")
    fun updateUserInsurance(@RequestBody body: InsuranceRequest): HttpStatus{
        if(body.accessToken.isEmpty() && body.ownerId.isEmpty()){
            return HttpStatus.BAD_REQUEST
        }
        return insuranceService.amendInsurance(
            Insurance(
                updatedAt = Instant.now(),
                id = ObjectId(body.ownerId),
                provider = body.provider,
                coverage = body.coverage,
                policyNumber = body.policyNumber
            ),
            accessToken = body.accessToken
        )
    }
}