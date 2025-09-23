package org.lifetrack.lifetrackspring.utils

import org.bson.types.ObjectId
import org.lifetrack.lifetrackspring.services.JwtService

import org.springframework.stereotype.Component

@Component
class ValidationUtil(
    private val jwtService: JwtService
) {

    fun validateRequestFromUser(userId: ObjectId, accessToken: String): Boolean {
        if (!jwtService.validateAccessToken(accessToken)) {
            return false
        }
        if (jwtService.parseUserIdFromToken(accessToken) != userId.toString()) {
            return false
        }
        return true
    }
}