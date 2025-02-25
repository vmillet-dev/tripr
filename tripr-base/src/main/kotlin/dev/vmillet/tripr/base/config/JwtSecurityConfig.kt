package dev.vmillet.tripr.base.config

import dev.vmillet.tripr.base.service.JwtUserDetailsService
import dev.vmillet.tripr.base.util.UserRoles
import java.lang.Exception
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
class JwtSecurityConfig {

    @Bean
    fun jwtAuthenticationManager(passwordEncoder: PasswordEncoder,
            jwtUserDetailsService: JwtUserDetailsService): AuthenticationManager {
        val jwtAuthenticationManager = DaoAuthenticationProvider(passwordEncoder)
        jwtAuthenticationManager.setUserDetailsService(jwtUserDetailsService)
        return ProviderManager(jwtAuthenticationManager)
    }

    @Bean
    @Order(10)
    @Throws(Exception::class)
    fun jwtFilterChain(
        http: HttpSecurity,
        @Qualifier("jwtAuthenticationManager") jwtAuthenticationManager: AuthenticationManager,
        jwtRequestFilter: JwtRequestFilter
    ): SecurityFilterChain = http.securityMatcher("/swagger-ui.html", "/swagger-ui/**",
            "/v3/api-docs/**")
            .cors(Customizer.withDefaults())
            .csrf { csrf -> csrf.disable() }
            .authorizeHttpRequests { authorize ->
            authorize.anyRequest().hasAuthority(UserRoles.ADMIN) }
            .authenticationManager(jwtAuthenticationManager)
            .sessionManagement { session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()

}
