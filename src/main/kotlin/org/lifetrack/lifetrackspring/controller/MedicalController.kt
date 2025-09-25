package org.lifetrack.lifetrackspring.controller

import org.apache.coyote.BadRequestException
import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.BRequest
import org.lifetrack.lifetrackspring.database.model.data.MedicalHistory
import org.lifetrack.lifetrackspring.database.model.data.MedicalPRequest
import org.lifetrack.lifetrackspring.database.model.data.MedicalResponse
import org.lifetrack.lifetrackspring.services.MedicalService
import org.lifetrack.lifetrackspring.utils.toMedicalResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/user/medical/")
class MedicalController(
    private val medicalService: MedicalService
) {
    @GetMapping
    fun getUserMedicalHistory(@RequestBody body: BRequest): MedicalResponse{
        if (body.accessToken.isEmpty() && body.userId.isEmpty()){
            throw BadRequestException(HttpStatus.BAD_REQUEST.toString())
        }
        return medicalService.retrieveMedicalHistory(ObjectId(body.userId), body.accessToken)
            .toMedicalResponse()
    }

    @PostMapping
    fun initUserMedicalHistory(@RequestParam accessToken: String, @RequestBody body: MedicalPRequest): HttpStatus{
        if(accessToken.isEmpty() && body.ownerId.isEmpty()){
            return HttpStatus.UNAUTHORIZED
        }
        val userMedicalHistory = MedicalHistory(
            ownerId = ObjectId(body.ownerId),
            id = ObjectId.get(),
            allergies = body.allergies,
            chronicConditions = body.chronicConditions,
            pastSurgeries = body.pastSurgeries,
            familyHistory = body.familyHistory,
            updatedAt = Instant.now(),
            visits = body.visits
        )
        return medicalService.createMedicalHistory(userMedicalHistory.ownerId, userMedicalHistory, accessToken)
    }

    @PatchMapping
    fun updateUserMedicalHistory(){

    }

    @DeleteMapping
    fun deleteUserMedicalHistory( @RequestBody body: BRequest): HttpStatus{
        if (body.accessToken.isEmpty() && body.userId.isEmpty()){
            throw BadRequestException(HttpStatus.BAD_REQUEST.toString())
        }
        return medicalService.eraseMedicalHistory(ObjectId(body.userId), body.accessToken)
    }
}