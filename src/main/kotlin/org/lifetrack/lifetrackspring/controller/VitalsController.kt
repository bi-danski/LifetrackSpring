package org.lifetrack.lifetrackspring.controller

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.UserVitals
import org.lifetrack.lifetrackspring.database.model.dto.VitalsRequest
import org.lifetrack.lifetrackspring.database.model.dto.VitalsResponse
import org.lifetrack.lifetrackspring.services.JwtService
import org.lifetrack.lifetrackspring.services.UserService
import org.lifetrack.lifetrackspring.services.VitalService
import org.lifetrack.lifetrackspring.utils.toVitalsResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/iot/vitals")
class VitalsController(
    private val vitalService: VitalService,
    private val jwtService: JwtService,
    private val userService: UserService,
) {
    @GetMapping
    fun getUserVitals(@RequestParam userId:String, @RequestBody vBody: VitalsRequest): VitalsResponse{
        return vitalService.retrieveVitals(
            ObjectId(userId),
            accessToken = vBody.accessToken
        )
            .toVitalsResponse()
    }

    @PostMapping
    fun saveUserVitals(@RequestBody vBody: VitalsRequest): HttpStatus{
        if (vBody.accessToken.isEmpty() || vBody.vitalsData == null ) {
            return HttpStatus.BAD_REQUEST
        }
        val userId = jwtService.parseUserIdFromToken(vBody.accessToken)
        val response = userService.findUserById(ObjectId(userId), accessToken = vBody.accessToken)
        if (response.compareTo(HttpStatus.FOUND) != 0 && userId.isEmpty()){
            return HttpStatus.BAD_REQUEST
        }
        vitalService.storeVitals(
            UserVitals(
                id = ObjectId(userId),
                pulse = vBody.vitalsData.pulse,
                bloodPressure = vBody.vitalsData.bloodPressure,
                bodyTemperature = vBody.vitalsData.bodyTemperature,
                respiratoryRate = vBody.vitalsData.respiratoryRate,
                oxygenSaturation = vBody.vitalsData.oxygenSaturation
            ),
            accessToken = vBody.accessToken
        )
        return HttpStatus.OK
    }

    @DeleteMapping
    fun deleteUserVitals(@RequestBody vitalBody: VitalsRequest): HttpStatus{
        if (vitalBody.resId.isNullOrEmpty() && vitalBody.accessToken.isEmpty()){
            return HttpStatus.BAD_REQUEST
        }
        val resourceId = ObjectId(vitalBody.resId)
        vitalService.eraseVitals(resourceId, vitalBody.accessToken)
        return HttpStatus.OK
    }

    @PatchMapping
    fun updateUserVitals(@RequestBody nwVitalBody: VitalsRequest): HttpStatus{
        if (nwVitalBody.vitalsData == null && nwVitalBody.accessToken.isEmpty() ) {
            return HttpStatus.BAD_REQUEST
        }
        if(!jwtService.validateAccessToken(nwVitalBody.accessToken)) {
            return HttpStatus.UNAUTHORIZED
        }
        val userId = jwtService.parseUserIdFromToken(nwVitalBody.accessToken)
        if (userService.findUserById(ObjectId(userId), accessToken = nwVitalBody.accessToken).compareTo(HttpStatus.FOUND) != 0 && userId.isEmpty()){
            return HttpStatus.BAD_REQUEST
        }
        vitalService.amendVitals(
            UserVitals( id = ObjectId(userId),
                pulse = nwVitalBody.vitalsData?.pulse,
                bloodPressure = nwVitalBody.vitalsData?.bloodPressure,
                oxygenSaturation = nwVitalBody.vitalsData?.oxygenSaturation,
                bodyTemperature = nwVitalBody.vitalsData?.bodyTemperature,
                respiratoryRate = nwVitalBody.vitalsData?.respiratoryRate,
                lastUpdatedAt = Instant.now()
                ),
            nwVitalBody.accessToken
        )
        return HttpStatus.OK
    }
}