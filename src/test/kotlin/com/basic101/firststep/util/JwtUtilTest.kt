package com.basic101.firststep.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class JwtUtilTest {

    @Test
    fun generateJwtTokenAndVerify() {

        val signedSecret = "sjgdjgdjasgjagfjagjgfjasgfjsgfjasfgjasfgjasfgjasgf"

        val token = JwtUtil.generateJwtToken(
            username = "Vikas",
            signedSecret = signedSecret,
            roles = listOf(
                "ADMIN"
            )
        )

        assertNotNull(token)

        val claims = JwtUtil.validateJwtToken(
            token, signedSecret
        )

        assertEquals(claims.subject, "Vikas")

    }

}