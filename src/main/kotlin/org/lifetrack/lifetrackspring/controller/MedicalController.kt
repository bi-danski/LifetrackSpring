package org.lifetrack.lifetrackspring.controller

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.dto.*
import org.lifetrack.lifetrackspring.database.model.helpers.toMedicalResponse
import org.lifetrack.lifetrackspring.service.MedicalService
import org.lifetrack.lifetrackspring.service.VisitService
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user/medical/")
class MedicalController(
    private val medicalService: MedicalService,
    private val visitService: VisitService
) {
    private val userId = ObjectId(SecurityContextHolder.getContext().authentication.principal as String)

    @GetMapping("/history")
    fun getUserMedicalHistory(): MedicalResponse{
        return medicalService.retrieveMedicalHistory(userId)
            .toMedicalResponse()
    }

    @PostMapping("/history")
    fun initUserMedicalHistory(@RequestBody body: MedicalPRequest): HttpStatus{
        return medicalService.createMedicalHistory(userId, body)
    }

    @PatchMapping("/history")
    fun updateUserMedicalHistory(@RequestBody body: MedicalPRequest): HttpStatus{
         return medicalService.amendMedicalHistory(userId, body)
    }

    @DeleteMapping("/history")
    fun deleteUserMedicalHistory(): HttpStatus{
        return medicalService.eraseMedicalHistory(userId)
    }

    // Medical LabTest

    @GetMapping("/lab")
    fun getUserMedicalLabTestResults(): MutableList<LabResultUpdate>{
        val allResults = visitService.retrieveVisits(userId)
        return visitService.extractAllLabResults(allResults)
    }

    @PatchMapping("/lab")
    fun updateUserMedicalLabResults(@RequestBody body: UserLabRequest): HttpStatus{
        return try {
            visitService.amendVisitLabResults(userId, userRequest = body)
            HttpStatus.OK
        }catch (_: Exception) {
            HttpStatus.SERVICE_UNAVAILABLE
        }
    }

    @DeleteMapping
    fun deleteUserMedicalLabResult(body: VRequest): HttpStatus{
        return try {
            visitService.eraseVisitLabResults(
                ObjectId(body.visitId),
                ObjectId(body.id),
                userId
            )
        }catch (_: Exception){
            HttpStatus.SERVICE_UNAVAILABLE
        }
    }

    // Medical Prescriptions

    @GetMapping("/prescriptions")
    fun getUserMedicalPrescription(): MutableList<PrescriptionUpdate>{
        val userVisits = visitService.retrieveVisits(userId)
        return visitService.extractAllPrescriptions(userVisits)
    }

    @PatchMapping("/prescriptions")
    fun updateUserMedicalPrescriptions(@RequestBody bodyRequest: UserPrescriptionRequest): HttpStatus{
        return try {
            visitService.amendVisitPrescriptions(userId, bodyRequest)
        }catch (_: Exception){
            HttpStatus.SERVICE_UNAVAILABLE
        }
    }


    @DeleteMapping(path=["/prescriptions/{id}"])
    fun deleteUserMedicalPrescription( body: VRequest): HttpStatus{
        return try {
            visitService.eraseVisitPrescription(
                ObjectId(body.visitId),
                ObjectId(body.id),
                userId
            )
        }catch (_: Exception){
            HttpStatus.SERVICE_UNAVAILABLE
        }
    }

    // Medical Diagnosis

    @GetMapping("/diagnosis")
    fun getUserMedicalDiagnosis(): MutableList<DiagnosisUpdate>{
        val userVisits = visitService.retrieveVisits(userId)
        return visitService.extractAllDiagnosis(userVisits)
    }

    @PatchMapping("/diagnosis")
    fun updateUserMedicalDiagnosis(@RequestBody body: UserDiagnosisRequest): HttpStatus{
        return try {
            visitService.amendVisitDiagnosis(userId, body)
        }catch (_: Exception){
            HttpStatus.SERVICE_UNAVAILABLE
        }
    }

    @DeleteMapping
    fun deleteUserMedicalDiagnosis( body: VRequest): HttpStatus{
        return try {
            visitService.eraseVisitDiagnosis(
                ObjectId(body.visitId),
                ObjectId(body.id),
                userId
            )
        }catch (_: Exception){
            HttpStatus.SERVICE_UNAVAILABLE
        }
    }

    // Visits

    @PostMapping("/visit")
    fun initUserMedicalVisits(@RequestBody visitBody: VisitUpdate): HttpStatus{
        return try{
            visitService.createVisitsDocument(userId, visitBody)
        }catch (_: Exception){
            HttpStatus.SERVICE_UNAVAILABLE
        }
    }

    @PatchMapping("/visit")
    fun updateUserMedicalVisitInfo(@RequestBody body: UserVisitRequest): HttpStatus{
          return try{
            visitService.amendVisitInfo(visitId = ObjectId(body.visitId),
                userId = userId,
                userVisitData = body.visitInfo
            )
            HttpStatus.OK
        }catch (_: Exception){
            HttpStatus.SERVICE_UNAVAILABLE
        }
    }

    @DeleteMapping("/visit/{visitId}")
    fun deleteUserMedicalVisit(@PathVariable visitId: String): HttpStatus{
        return try {
            visitService.eraseVisit(ObjectId(visitId), userId)
        }catch (_: Exception){
            HttpStatus.SERVICE_UNAVAILABLE
        }
    }
}