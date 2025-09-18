package org.lifetrack.lifetrackspring.services

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Service
class JwtService(
    @param:Value("\${jwt.secret}") private val jwtSecret: String
) {
    private val accessTokenValidityMs: Long = 24L * 60 * 60 * 1000
    private val refreshTokenValidityMs: Long = 2L * 24L * 60 * 60 * 1000
    @OptIn(ExperimentalEncodingApi::class)
    private val jwtSecretKey = Keys.hmacShaKeyFor(Base64.decode(jwtSecret))

    private fun generateToken(
        expiration: Long,
        subjectId: String,
        type: String
    ): String{
        val now  = Date()
        return Jwts.builder()
            .subject(subjectId)
            .claim("type", type)
            .issuedAt(now)
            .expiration(Date(now.time + expiration))
            .signWith(jwtSecretKey, Jwts.SIG.HS256)
            .compact()
    }

    fun generateAccessToken(uid: ObjectId): String{
        return generateToken(expiration = accessTokenValidityMs,
            subjectId = uid.toHexString(),
            type = "access"
        )
    }

    fun generateRefreshToken(uid: ObjectId): String{
        return generateToken(expiration = refreshTokenValidityMs,
            subjectId = uid.toHexString(),
            type = "refresh"
            )
    }

    fun parseJwtTokenClaims(jwtToken: String): Claims?{
        if (jwtToken.startsWith("Bearer ")){
            jwtToken.removePrefix("Bearer ")
        }
        return Jwts.parser()
            .verifyWith(jwtSecretKey)
            .build()
            .parseSignedClaims(jwtToken)
            .payload
    }

    fun parseUserIdFromToken(jwtToken: String): String{
        val claims = parseJwtTokenClaims(jwtToken) ?: throw IllegalArgumentException("Invalid Token")
        return claims.subject
    }

    fun validateAccessToken(jwtAccessToken: String): Boolean{
        val accessClaims = parseJwtTokenClaims(jwtAccessToken)
        if (accessClaims.isNullOrEmpty()){
            return false
        }
        return accessClaims["type"] == "access"
    }

    fun validateRefreshToken(jwtRefreshToken: String): Boolean{
        val refreshClaims = parseJwtTokenClaims(jwtRefreshToken)
        if (refreshClaims.isNullOrEmpty()){
            return false
        }
        return refreshClaims["type"] == "refresh"
    }
}