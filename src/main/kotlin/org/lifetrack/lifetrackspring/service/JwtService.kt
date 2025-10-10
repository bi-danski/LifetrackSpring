package org.lifetrack.lifetrackspring.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Service
class JwtService(
    @param:Value("\${jwt.siri}") private val jwtSiri: String
) {
    private val accessTokenValidityMs: Long = TimeUnit.MINUTES.toMillis(15)
    private val refreshTokenValidityMs: Long = TimeUnit.HOURS.toMillis(1)

    @OptIn(ExperimentalEncodingApi::class)
    private val jwtSecretKey = Keys.hmacShaKeyFor(Base64.decode(jwtSiri))

    private fun generateToken(
        expiration: Long,
        subjectId: String,
        type: String
    ): String {
        val now = Date()
        return Jwts.builder()
            .subject(subjectId)
            .claim("type", type)
            .issuedAt(now)
            .expiration(Date(now.time + expiration))
            .signWith(jwtSecretKey, Jwts.SIG.HS256)
            .compact()
    }

    fun generateAccessToken(uid: ObjectId): String =
        generateToken(
            accessTokenValidityMs,
            uid.toHexString(),
            "access"
        )

    fun generateRefreshToken(uid: ObjectId): String =
        generateToken(
            refreshTokenValidityMs,
            uid.toHexString(),
            "refresh"
        )

    fun parseJwtTokenClaims(jwtToken: String): Claims? {
        val token = if (jwtToken.startsWith("Bearer ")) {
            jwtToken.removePrefix("Bearer ")
        } else jwtToken
        return Jwts.parser()
            .verifyWith(jwtSecretKey)
            .build()
            .parseSignedClaims(token)
            .payload
    }

    fun parseUserIdFromToken(jwtToken: String): String {
        val claims = parseJwtTokenClaims(jwtToken) ?: throw IllegalArgumentException("Invalid Token")
        return claims.subject
    }

    fun validateAccessToken(jwtAccessToken: String): Boolean {
        val claims = parseJwtTokenClaims(jwtAccessToken) ?: return false
        return claims["type"] == "access"
    }

    fun validateRefreshToken(jwtRefreshToken: String): Boolean {
        val claims = parseJwtTokenClaims(jwtRefreshToken) ?: return false
        return claims["type"] == "refresh"
    }
}
