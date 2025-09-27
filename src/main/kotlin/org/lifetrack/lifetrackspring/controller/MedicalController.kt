package org.lifetrack.lifetrackspring.controller

import org.apache.coyote.BadRequestException
import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.MedicalHistory
import org.lifetrack.lifetrackspring.database.model.dto.*
import org.lifetrack.lifetrackspring.database.model.helpers.toMedicalResponse
import org.lifetrack.lifetrackspring.services.MedicalService
import org.lifetrack.lifetrackspring.services.VisitService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/user/medical/")
class MedicalController(
    private val medicalService: MedicalService,
    private val visitService: VisitService
) {
    @GetMapping("/history")

    fun getUserMedicalHistory(@RequestBody body: BRequest): MedicalResponse{
        if (body.accessToken.isEmpty() && body.userId.isEmpty()){
            throw BadRequestException(HttpStatus.BAD_REQUEST.toString())
        }
        return medicalService.retrieveMedicalHistory(ObjectId(body.userId), body.accessToken)
            .toMedicalResponse()
    }

    @PostMapping("/history")
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
        )
        return medicalService.createMedicalHistory(userMedicalHistory.ownerId, userMedicalHistory, accessToken)
    }

    @PatchMapping("/history")
    fun updateUserMedicalHistory(@RequestParam accessToken: String, @RequestBody body: MedicalPRequest): HttpStatus{
        if (accessToken.isEmpty() && body.ownerId.isEmpty()){
            throw BadRequestException(HttpStatus.BAD_REQUEST.toString())
        }
        return medicalService.amendMedicalHistory(body, accessToken = accessToken)
    }

    @DeleteMapping("/history")
    fun deleteUserMedicalHistory( @RequestBody body: BRequest): HttpStatus{
        if (body.accessToken.isEmpty() && body.userId.isEmpty()){
            throw BadRequestException(HttpStatus.BAD_REQUEST.toString())
        }
        return medicalService.eraseMedicalHistory(ObjectId(body.userId), body.accessToken)
    }


    // Medical LabTest


    @GetMapping("/lab")
    fun getUserMedicalLabTestResults(@RequestBody body: BRequest): MutableList<LabResultUpdate>{
        val allResults = visitService.retrieveVisits(ObjectId(body.userId), body.accessToken)
        return visitService.extractAllLabResults(allResults)
    }

    @PatchMapping("/lab")
    fun updateUserMedicalLabResults(@RequestBody body: UserLabRequest): HttpStatus{
        if (body.userId.isEmpty() && body.accessToken.isEmpty() && body.labResultId.isEmpty() && body.visitId.isEmpty()){
            return HttpStatus.BAD_REQUEST
        }
        return try {
            visitService.amendVisitLabResults(
                userRequest = body
            )
            HttpStatus.OK
        }catch (_: Exception) {
            HttpStatus.SERVICE_UNAVAILABLE
        }
    }

    @DeleteMapping(path=["/{id}"])
    fun deleteUserMedicalLabResult(@PathVariable id: String, body: BRequest): HttpStatus{
        // ToDo
        return HttpStatus.SERVICE_UNAVAILABLE
    }


    // Medical Prescriptions


    @GetMapping("/prescriptions")
    fun getUserMedicalPrescription(@RequestBody body: BRequest ): MutableList<PrescriptionUpdate>{
        val userVisits = visitService.retrieveVisits(ObjectId(body.userId), body.accessToken)
        return visitService.extractAllPrescriptions(userVisits)
    }

    @PatchMapping("/prescriptions")
    fun updateUserMedicalPrescriptions(@RequestBody body: UserPrescriptionRequest): HttpStatus{
        if(body.accessToken.isEmpty() && body.prescriptionId.isEmpty() && body.visitId.isEmpty() && body.prescriptionId.isEmpty()){
            return HttpStatus.BAD_REQUEST
        }
        return visitService.amendVisitPrescriptions(body)
    }

    @DeleteMapping(path=["/prescriptions/{id}"])
    fun deleteUserMedicalPrescription(@PathVariable id: String, body: BRequest): HttpStatus{
        // ToDo
        return HttpStatus.SERVICE_UNAVAILABLE
    }


    // Medical Diagnosis


    @GetMapping("/diagnosis")
    fun getUserMedicalDiagnosis(@RequestBody body: BRequest): MutableList<DiagnosisUpdate>{
        val userVisits = visitService.retrieveVisits(ObjectId(body.userId), body.accessToken)
        return visitService.extractAllDiagnosis(userVisits)

    }

    @PatchMapping("/diagnosis")
    fun updateUserMedicalDiagnosis(@RequestBody body: UserDiagnosisRequest): HttpStatus{
        if(body.userId.isEmpty() && body.visitId.isEmpty() && body.diagnosisId.isEmpty() && body.accessToken.isEmpty()){
            return HttpStatus.BAD_REQUEST
        }
        return try {
            visitService.amendVisitDiagnosis(
                userRequest = body
            )
        }catch (_: Exception){
            HttpStatus.SERVICE_UNAVAILABLE
        }
    }

    @DeleteMapping(path=["/diagnosis/{id}"])
    fun deleteUserMedicalDiagnosis(@PathVariable id: String, body: BRequest): HttpStatus{
        // ToDo
        return HttpStatus.SERVICE_UNAVAILABLE
    }


    // Visits


    @PostMapping("/visit")
    fun initUserMedicalVisits(@RequestParam userId: String, accessToken: String, @RequestBody visitBody: VisitUpdate): HttpStatus{
        if(userId.isEmpty() && accessToken.isEmpty()){
            return HttpStatus.BAD_REQUEST
        }
        return try{
            visitService.createVisitsDocument(ObjectId(userId), accessToken, visitBody)
        }catch (_: Exception){
            HttpStatus.SERVICE_UNAVAILABLE
        }
    }

    @PatchMapping("/visit")
    fun updateUserMedicalVisitInfo(@RequestBody body: UserVisitRequest): HttpStatus{
        if(body.userId.isEmpty() && body.visitId.isEmpty() && body.accessToken.isEmpty()){
            return HttpStatus.BAD_REQUEST
        }
        return try{
            visitService.amendVisitInfo(visitId = ObjectId(body.visitId),
                userId = ObjectId(body.userId),
                accessToken = body.accessToken,
                userVisitData = body.visitInfo
            )
            HttpStatus.OK
        }catch (_: Exception){
            HttpStatus.SERVICE_UNAVAILABLE
        }
    }

    @DeleteMapping("/visit")
    fun deleteUserMedicalVisit(@RequestBody body: CommonRequest): HttpStatus{
        if (body.id.isEmpty() && body.accessToken.isEmpty() && body.userId.isEmpty()){
            return HttpStatus.UNAUTHORIZED
        }
        return visitService.eraseVisit(
             ObjectId(body.id),
            ObjectId(body.userId),
            body.accessToken )
    }


}