package org.lifetrack.lifetrackspring.services

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.model.data.UserVitals
import org.lifetrack.lifetrackspring.database.repository.VitalsRepository
import org.lifetrack.lifetrackspring.utils.Utilities
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class VitalService(
    private val vitalsRepository: VitalsRepository,
    private val utilities: Utilities
) {
    @Transactional
    fun amendVitals(nwUserVitals: UserVitals, accessToken: String): HttpStatus{
        if(!utilities.validateRequestFromUser(nwUserVitals.id, accessToken)) {
            return HttpStatus.UNAUTHORIZED
        }
        vitalsRepository.deleteById(nwUserVitals.id)
        vitalsRepository.save<UserVitals>(nwUserVitals)
        return HttpStatus.OK
    }

    fun storeVitals(userVitals: UserVitals, accessToken: String): UserVitals{
        if (!utilities.validateRequestFromUser(userVitals.id, accessToken)){
            throw AccessDeniedException(
                HttpStatus.UNAUTHORIZED.toString()
            )
        }
        return vitalsRepository.save<UserVitals>(userVitals)
    }

    fun eraseVitals(userId: ObjectId, accessToken: String): HttpStatus{
        if (!utilities.validateRequestFromUser(userId, accessToken)){
            return HttpStatus.UNAUTHORIZED
            }
        if (vitalsRepository.findById(userId).isEmpty){
            return HttpStatus.NOT_FOUND
        }
        vitalsRepository.deleteUsersVitalsById(userId)
        return HttpStatus.OK
    }

    fun retrieveVitals(userId: ObjectId, accessToken: String): UserVitals{
        if (!utilities.validateRequestFromUser(userId, accessToken)){
            throw AccessDeniedException(
                HttpStatus.UNAUTHORIZED.toString()
            )
        }
        return vitalsRepository.findUsersVitalsById(userId)
    }

}