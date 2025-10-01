package org.lifetrack.lifetrackspring.service

import com.mongodb.MongoException
import com.mongodb.MongoInternalException
import com.mongodb.MongoQueryException
import com.mongodb.MongoWriteException
import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.UserVitals
import org.lifetrack.lifetrackspring.database.model.dto.VitalsDataRequest
import org.lifetrack.lifetrackspring.database.repository.VitalsRepository
import org.lifetrack.lifetrackspring.exception.ResourceNotFound
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class VitalService(
    private val vitalsRepository: VitalsRepository,
) {
    fun amendVitals(userId: ObjectId , updatedVitals: VitalsDataRequest): HttpStatus{
        return try {
            if (!vitalsRepository.existsUserVitalsByOwnerId(userId)){
                return HttpStatus.NOT_FOUND
            }
            val vitalUpdate = vitalsRepository.findUsersVitalsByOwnerId(userId).copy(
                pulse = updatedVitals.pulse,
                bloodPressure = updatedVitals.bloodPressure,
                bodyTemperature = updatedVitals.bodyTemperature,
                respiratoryRate = updatedVitals.respiratoryRate,
                oxygenSaturation = updatedVitals.oxygenSaturation,
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

    fun storeVitals(newVitals: VitalsDataRequest, userId: ObjectId): HttpStatus {
        if (vitalsRepository.existsUserVitalsByOwnerId(userId)){
            return HttpStatus.CONFLICT
        }
        return try {
            vitalsRepository.save(
                UserVitals(
                    id = ObjectId.get(),
                    ownerId = userId,
                    pulse = newVitals.pulse,
                    bloodPressure = newVitals.bloodPressure,
                    bodyTemperature = newVitals.bodyTemperature,
                    respiratoryRate = newVitals.respiratoryRate,
                    oxygenSaturation = newVitals.oxygenSaturation,
                    createdAt = Instant.now(),
                    lastUpdatedAt = Instant.now()
                )
            )
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

    fun eraseVitals(userId: ObjectId): HttpStatus{
        if (!vitalsRepository.existsUserVitalsByOwnerId(userId)){
            return HttpStatus.NOT_FOUND
        }
        return try{
            vitalsRepository.deleteUsersVitalsByOwnerId(userId)
            HttpStatus.OK
        }catch (_: MongoQueryException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: MongoException){
            return HttpStatus.INTERNAL_SERVER_ERROR
        }catch (_: Exception){
            return HttpStatus.SERVICE_UNAVAILABLE
        }
    }

    fun retrieveVitals(userId: ObjectId): UserVitals {
        if (!vitalsRepository.existsUserVitalsByOwnerId(userId)){
            throw ResourceNotFound(HttpStatus.NOT_FOUND.toString())
        }
        return vitalsRepository.findUsersVitalsByOwnerId(userId)
    }
}