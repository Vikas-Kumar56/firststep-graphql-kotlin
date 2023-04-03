package com.basic101.firststep.security

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationManager(
    private val customAuthenticationProvider: CustomAuthenticationProvider
) : AuthenticationManager {
    override fun authenticate(authentication: Authentication?): Authentication {
        if (authentication !== null) {
            if (customAuthenticationProvider.supports(authentication = authentication.javaClass)) {
                return customAuthenticationProvider.authenticate(authentication = authentication)
            }
        }

        throw BadCredentialsException("Bad Cred!")
    }
}