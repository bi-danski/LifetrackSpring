package org.lifetrack.lifetrackspring.security

import org.springframework.stereotype.Component
import java.security.MessageDigest
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Component
class TokenEncoder {
    private val digest = MessageDigest.getInstance("SHA-512")

    @OptIn(ExperimentalEncodingApi::class)
    fun hashToken(jwtToken: String): String{
       val jwtTokenDigest =  digest.digest(jwtToken.toByteArray())
        return Base64.encode(jwtTokenDigest)
    }
}