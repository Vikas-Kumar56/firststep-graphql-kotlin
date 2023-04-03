package com.basic101.firststep.filter

import com.basic101.firststep.security.CustomAuthentication
import com.basic101.firststep.security.CustomAuthenticationManager
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(
    private val customAuthenticationManager: CustomAuthenticationManager
): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        // check do we have Authorization header
        // yes -> Create Authentication(Which is not authenticated)
        // Authentication Manager(authentication) -> authentication provider (jwt verify)
        // authentication (which is authenticated) from manager
        // set authentication to security context

        if (request.getHeader("Authorization") == null) {
            filterChain.doFilter(request, response)
            return
        }

        // Bearer should be part of request as best practices.
        val authentication = CustomAuthentication(
            authentication = false,
            token = request.getHeader("Authorization")
        )

        val authenticated = customAuthenticationManager.authenticate(authentication)

        if(
            authenticated.isAuthenticated
        ) {
            val context = SecurityContextHolder.createEmptyContext()
            context.authentication = authenticated
            SecurityContextHolder.setContext(context)
            filterChain.doFilter(request, response)
        }
    }
}