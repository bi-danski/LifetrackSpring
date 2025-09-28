package org.lifetrack.lifetrackspring.service

import com.mongodb.MongoException
import com.mongodb.MongoInternalException
import com.mongodb.MongoQueryException
import com.mongodb.MongoWriteException
import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.UserVitals
import org.lifetrack.lifetrackspring.database.repository.VitalsRepository
import org.lifetrack.lifetrackspring.utils.ValidationUtil
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class VitalService(
    private val vitalsRepository: VitalsRepository,
    private val validationUtil: ValidationUtil
) {
    @Transactional
    fun amendVitals(nwUserVitals: UserVitals, accessToken: String): HttpStatus{
        if(!validationUtil.validateRequestFromUser(nwUserVitals.id, accessToken)) {
            return HttpStatus.UNAUTHORIZED
        }
        return try {
            val response = vitalsRepository.findUsersVitalsById(nwUserVitals.id)
            val vitalUpdate = response.copy(
                pulse = nwUserVitals.pulse,
                bloodPressure = nwUserVitals.bloodPressure,
                bodyTemperature = nwUserVitals.bodyTemperature,
                respiratoryRate = nwUserVitals.respiratoryRate,
                oxygenSaturation = nwUserVitals.oxygenSaturation,
                lastUpdatedAt = Instant.now()
            )
            vitalsRepository.save(vitalUpdate)
            HttpStatus.OK
        }catch (_: MongoWriteException){
            return HttpStatus.UNPROCESSABLE_ENTITY
        }catch (_: MongoException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: MongoInternalException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: Exception){
            return HttpStatus.SERVICE_UNAVAILABLE
        }
    }

    fun storeVitals(userVitals: UserVitals, accessToken: String): HttpStatus {
        if (!validationUtil.validateRequestFromUser(userVitals.id, accessToken)) {
            return HttpStatus.UNAUTHORIZED
        }
        if (vitalsRepository.findById(userVitals.id).isPresent) {
            return HttpStatus.CONFLICT
        }
        return try {
            vitalsRepository.save(userVitals)
            HttpStatus.CREATED
        } catch (_: MongoWriteException) {
            HttpStatus.UNPROCESSABLE_ENTITY
        } catch (_: MongoInternalException) {
            HttpStatus.INTERNAL_SERVER_ERROR
        } catch (_: MongoException) {
            HttpStatus.INTERNAL_SERVER_ERROR
        } catch (_: Exception) {
            HttpStatus.SERVICE_UNAVAILABLE
        }
    }

    fun eraseVitals(userId: ObjectId, accessToken: String): HttpStatus{
        if (!validationUtil.validateRequestFromUser(userId, accessToken)){
            return HttpStatus.UNAUTHORIZED
            }
        if (vitalsRepository.findById(userId).isEmpty){
            return HttpStatus.NOT_FOUND
        }
        return try{
            vitalsRepository.deleteUsersVitalsById(userId)
            HttpStatus.OK
        }catch (_: MongoQueryException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: MongoException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: Exception){
            return HttpStatus.SERVICE_UNAVAILABLE
        }
    }

    fun retrieveVitals(userId: ObjectId, accessToken: String): UserVitals {
        if (!validationUtil.validateRequestFromUser(userId, accessToken)) {
            throw AccessDeniedException(
                HttpStatus.UNAUTHORIZED.toString()
            )
        }
        val existing = vitalsRepository.findById(userId)
        if (existing.isEmpty) {
            throw NoSuchElementException(HttpStatus.NOT_FOUND.toString())
        }
        return existing.get()
    }
}