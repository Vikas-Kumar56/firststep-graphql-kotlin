package com.basic101.firststep.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.time.Instant
import java.util.*
import javax.crypto.SecretKey

object JwtUtil {

    fun generateJwtToken(
        username: String,
        signedSecret: String,
        roles: List<String> = listOf()
    ): String {
        val claims = HashMap<String, Any>()
        claims["roles"] = roles

        val currentDate = Date.from(Instant.now())

        val key = getSecretKey(signedSecret)

        return Jwts.builder()
            .addClaims(claims)
            .setSubject(username)
            .setIssuedAt(currentDate)
            .setExpiration(
                Date.from(
                    currentDate.toInstant().plusMillis(
                        1_000 * 60 * 60 * 24
                    )
                )
            )
            .setId(UUID.randomUUID().toString())
            .signWith(key)
            .compact()

    }

    private fun getSecretKey(signedSecret: String): SecretKey =
        Keys.hmacShaKeyFor(signedSecret.toByteArray(Charsets.UTF_8))

    fun validateJwtToken(
        token: String,
        signedSecret: String
    ) = Jwts.parserBuilder()
        .setSigningKey(getSecretKey(signedSecret))
        .build()
        .parseClaimsJws(token)
        .body

}