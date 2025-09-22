package org.lifetrack.lifetrackspring.services

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.delegate.VitalsRepository
import org.lifetrack.lifetrackspring.database.model.data.UserVitals
import org.lifetrack.lifetrackspring.database.repository.VitalsMongoRepository
import org.lifetrack.lifetrackspring.utils.Utilities
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service

@Service
class VitalService(
    private val vitalsMongoRepository: VitalsMongoRepository,
    private val vitalsRepository: VitalsRepository,
    private val utilities: Utilities
) {
    fun amendVitals(nwUserVitals: UserVitals, accessToken: String): UserVitals{
        if (!utilities.validateRequestFromUser(nwUserVitals.id, accessToken)){
            throw AccessDeniedException(
                HttpStatus.UNAUTHORIZED.toString()
            )
        }
        return vitalsRepository.updateUserVitals(nwUserVitals, accessToken)
    }

    fun storeVitals(userVitals: UserVitals, accessToken: String): UserVitals{
        if (!utilities.validateRequestFromUser(userVitals.id, accessToken)){
            throw AccessDeniedException(
                HttpStatus.UNAUTHORIZED.toString()
            )
        }
        return vitalsMongoRepository.save<UserVitals>(userVitals)
    }

    fun eraseVitals(userId: ObjectId, accessToken: String): HttpStatus{
        if (!utilities.validateRequestFromUser(userId, accessToken)){
            return HttpStatus.UNAUTHORIZED
            }
        if (vitalsMongoRepository.findById(userId).isEmpty){
            return HttpStatus.NOT_FOUND
        }
        vitalsMongoRepository.deleteUsersVitalsById(userId)
        return HttpStatus.OK
    }

    fun retrieveVitals(userId: ObjectId, accessToken: String): UserVitals{
        if (!utilities.validateRequestFromUser(userId, accessToken)){
            throw AccessDeniedException(
                HttpStatus.UNAUTHORIZED.toString()
            )
        }
        return vitalsMongoRepository.findUsersVitalsById(userId)
    }



}