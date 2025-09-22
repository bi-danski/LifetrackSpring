package org.lifetrack.lifetrackspring.controller

import org.lifetrack.lifetrackspring.database.model.data.Insurance
import org.lifetrack.lifetrackspring.services.InsuranceService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user/")
class InsuranceController(
    private val insuranceService: InsuranceService
) {
    @GetMapping("/insure")
    fun getUserInsurance(){

    }
    @DeleteMapping(path=["{insuranceId}"])
    fun deleteUserInsurance(@PathVariable insuranceId: String, @RequestBody insBody: Insurance){

    }

    fun saveUserInsurance(){

    }

    fun updateUserInsurance(){

    }
}