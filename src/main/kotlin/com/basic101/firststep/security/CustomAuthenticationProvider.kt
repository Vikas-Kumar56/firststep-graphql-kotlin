package com.basic101.firststep.security

import com.basic101.firststep.util.JwtUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationProvider(
    @Value("\${key}")
    private val secretKey: String
): AuthenticationProvider {
    override fun authenticate(authentication: Authentication?): Authentication {

        return runCatching {
            val customAuthentication = authentication as CustomAuthentication

            val claims = JwtUtil.validateJwtToken(customAuthentication.credentials, secretKey)

            CustomAuthentication(
                authentication = true,
                username = claims.subject,
                roles = claims["roles"] as MutableCollection<String>
            )
        }.onFailure {
            throw BadCredentialsException("Bad Cred!")
        }.getOrThrow()

    }

    override fun supports(authentication: Class<*>?): Boolean {
        return CustomAuthentication::class.java == authentication
    }

}