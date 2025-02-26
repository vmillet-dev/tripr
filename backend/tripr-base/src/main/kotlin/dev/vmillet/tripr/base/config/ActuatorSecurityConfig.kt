package dev.vmillet.tripr.base.config

import dev.vmillet.tripr.base.service.ActuatorUserDetailsService
import dev.vmillet.tripr.base.util.UserRoles
import java.lang.Exception
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain


@Configuration
class ActuatorSecurityConfig {

    @Bean
    fun actuatorAuthenticationManager(passwordEncoder: PasswordEncoder,
            actuatorUserDetailsService: ActuatorUserDetailsService): AuthenticationManager {
        val actuatorAuthenticationManager = DaoAuthenticationProvider(passwordEncoder)
        actuatorAuthenticationManager.setUserDetailsService(actuatorUserDetailsService)
        return ProviderManager(actuatorAuthenticationManager)
    }

    @Bean
    @Order(20)
    @Throws(Exception::class)
    fun actuatorFilterChain(http: HttpSecurity, @Qualifier("actuatorAuthenticationManager")
            actuatorAuthenticationManager: AuthenticationManager): SecurityFilterChain =
            http.securityMatcher(EndpointRequest.toAnyEndpoint())
            .cors(Customizer.withDefaults())
            .csrf { csrf -> csrf.disable() }
            .authorizeHttpRequests { authorize ->
            authorize.anyRequest().hasAuthority(UserRoles.ACTUATOR) }
            .authenticationManager(actuatorAuthenticationManager)
            .httpBasic { basic -> basic.realmName("actuator realm") }
            .build()

}
