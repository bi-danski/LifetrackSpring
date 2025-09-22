package org.lifetrack.lifetrackspring.database.delegate

import org.lifetrack.lifetrackspring.database.model.data.UserVitals
import org.lifetrack.lifetrackspring.database.repository.VitalsMongoRepository
import org.lifetrack.lifetrackspring.services.JwtService
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class VitalsRepository(
    private val vitalsMongoRepository: VitalsMongoRepository,
    private val jwtService: JwtService
) {
        @Transactional
    fun updateUserVitals(newUserVitals: UserVitals, accessToken: String): UserVitals{
        if (!jwtService.validateAccessToken(accessToken)){
            throw IllegalArgumentException("Invalid Token" )
        }
        if (jwtService.parseUserIdFromToken(accessToken) != newUserVitals.id.toString()){
            throw AccessDeniedException(HttpStatus.UNAUTHORIZED.toString())
        }
        vitalsMongoRepository.deleteById(newUserVitals.id)
        return vitalsMongoRepository.save<UserVitals>(newUserVitals)
    }


}