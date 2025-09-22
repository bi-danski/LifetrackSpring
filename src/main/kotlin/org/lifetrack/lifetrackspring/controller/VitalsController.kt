package org.lifetrack.lifetrackspring.controller

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.UserVitals
import org.lifetrack.lifetrackspring.database.model.data.VitalsRequest
import org.lifetrack.lifetrackspring.database.model.data.VitalsResponse
import org.lifetrack.lifetrackspring.services.AuthService
import org.lifetrack.lifetrackspring.services.JwtService
import org.lifetrack.lifetrackspring.services.VitalService
import org.lifetrack.lifetrackspring.utils.toVitalsResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/iot/")
class VitalsController(
    private val vitalService: VitalService,
    private val jwtService: JwtService,
    private val authService: AuthService,
) {
    @GetMapping("/vitals")
    fun getUserVitals(@RequestParam userId:String, @RequestBody vBody: VitalsRequest): VitalsResponse{
        return vitalService.retrieveVitals(
            ObjectId(userId),
            accessToken = vBody.accessToken
        )
            .toVitalsResponse()
    }

    @PostMapping("/vitals")
    fun saveUserVitals(@RequestBody vBody: VitalsRequest): HttpStatus{
        if (vBody.vitalsData != null ){
            if(jwtService.validateAccessToken(vBody.accessToken)){

                val userId = jwtService.parseUserIdFromToken(vBody.accessToken)
                if (authService.getUserById(ObjectId(userId)).createdAt != null) {

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
                }else{
                    return HttpStatus.CONFLICT
                }
            }else{
                return HttpStatus.UNAUTHORIZED
            }
        }
        return HttpStatus.OK
    }
}