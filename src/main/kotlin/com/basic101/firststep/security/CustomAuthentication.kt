package com.basic101.firststep.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

class CustomAuthentication(
    private val authentication: Boolean = false,
    private val token: String? = null,
    private val username: String? = null,
    private val roles: MutableCollection<String> = mutableListOf()
): Authentication {

    override fun getName(): String {
        return username.orEmpty()
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles.map {
            SimpleGrantedAuthority(it)
        }.toMutableList()
    }

    override fun getCredentials(): String {
        return token.orEmpty()
    }

    override fun getDetails(): Any {
        return username.orEmpty()
    }

    override fun getPrincipal(): Any {
        return username.orEmpty()
    }

    override fun isAuthenticated(): Boolean {
        return authentication
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
       throw UnsupportedOperationException()
    }
}