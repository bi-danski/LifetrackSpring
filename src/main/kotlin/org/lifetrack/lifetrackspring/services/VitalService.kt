package org.lifetrack.lifetrackspring.services

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.database.delegate.VitalsRepository
import org.lifetrack.lifetrackspring.database.model.data.UserVitals
import org.lifetrack.lifetrackspring.database.repository.VitalsMongoRepository
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service

@Service
class VitalService(
    private val vitalsMongoRepository: VitalsMongoRepository,
    private val vitalsRepository: VitalsRepository,
    private val jwtService: JwtService
) {
    fun updateVitals(nwUserVitals: UserVitals, accessToken: String): UserVitals{
        if (!validateRequestFromUser(nwUserVitals.id, accessToken)){
            throw AccessDeniedException(
                HttpStatus.UNAUTHORIZED.toString()
            )
        }
        return vitalsRepository.updateUserVitals(nwUserVitals, accessToken)
    }

    fun storeVitals(userVitals: UserVitals, accessToken: String): UserVitals{
        if (!validateRequestFromUser(userVitals.id, accessToken)){
            throw AccessDeniedException(
                HttpStatus.UNAUTHORIZED.toString()
            )
        }
        return vitalsMongoRepository.save<UserVitals>(userVitals)
    }

    private fun eraseVitals(userVitals: UserVitals, accessToken: String): HttpStatus{
        if (!validateRequestFromUser(userVitals.id, accessToken)){
            return HttpStatus.UNAUTHORIZED
            }
        vitalsMongoRepository.deleteUsersVitalsById(userVitals.id)
        return HttpStatus.OK
    }

    fun retrieveVitals(userId: ObjectId, accessToken: String): UserVitals{
        if (!validateRequestFromUser(userId, accessToken)){
            throw AccessDeniedException(
                HttpStatus.UNAUTHORIZED.toString()
            )
        }
        return vitalsMongoRepository.findUsersVitalsById(userId)
    }

    private fun validateRequestFromUser(userId: ObjectId, accessToken: String): Boolean{
        if (!jwtService.validateAccessToken(accessToken)){
            return false
        }
        if (jwtService.parseUserIdFromToken(accessToken) != userId.toString()){
            return false
        }
        return true
    }

}