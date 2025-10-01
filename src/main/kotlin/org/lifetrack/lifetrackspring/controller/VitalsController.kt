package org.lifetrack.lifetrackspring.controller

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.dto.VitalsDataRequest
import org.lifetrack.lifetrackspring.database.model.dto.VitalsResponse
import org.lifetrack.lifetrackspring.database.model.helpers.toVitalsResponse
import org.lifetrack.lifetrackspring.service.VitalService
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/iot/vitals")
class VitalsController(
    private val vitalService: VitalService,
) {
    final fun userId() = ObjectId(SecurityContextHolder.getContext().authentication.principal as String)
    @GetMapping
    fun getUserVitals(): VitalsResponse{
        return vitalService.retrieveVitals(userId()).toVitalsResponse()
    }

    @PostMapping
    fun saveUserVitals(@RequestBody body: VitalsDataRequest): HttpStatus{
        vitalService.storeVitals(body, userId())
        return HttpStatus.OK
    }

    @DeleteMapping
    fun deleteUserVitals(): HttpStatus{
        vitalService.eraseVitals(userId())
        return HttpStatus.OK
    }

    @PatchMapping
    fun updateUserVitals(@RequestBody nwVitalBody: VitalsDataRequest): HttpStatus{
        vitalService.amendVitals(userId(), nwVitalBody)
        return HttpStatus.OK
    }
}