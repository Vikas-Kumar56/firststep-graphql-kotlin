package com.basic101.firststep.config

import com.basic101.firststep.filter.JwtFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class AppSecurity(
    private val jwtFilter: JwtFilter
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder(10)

    @Bean
    fun securityWebFilterChain(
        httpSecurity: HttpSecurity
    ): SecurityFilterChain {

        return httpSecurity
            .csrf { it.disable() }
            .addFilterAt(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
            .authorizeHttpRequests {
                it.anyRequest().permitAll()
            }
            .build()

    }
}