package dev.vmillet.tripr.base.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.authentication.AnonymousAuthenticationProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder


@Configuration
@EnableMethodSecurity(prePostEnabled = true)
class CommonSecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        // creates hashes with {bcrypt} prefix
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    @Primary
    fun noopAuthenticationManager(): AuthenticationManager =
            ProviderManager(AnonymousAuthenticationProvider("noop"))

}
