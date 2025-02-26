package dev.vmillet.tripr.base.rest

import dev.vmillet.tripr.base.model.AuthenticationRequest
import dev.vmillet.tripr.base.model.AuthenticationResponse
import dev.vmillet.tripr.base.model.RefreshTokenRequest
import dev.vmillet.tripr.base.service.JwtRefreshTokenService
import dev.vmillet.tripr.base.service.JwtTokenService
import dev.vmillet.tripr.base.service.JwtUserDetailsService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException


@RestController
class AuthenticationResource(
    @Qualifier("jwtAuthenticationManager")
    private val jwtAuthenticationManager: AuthenticationManager,
    private val jwtUserDetailsService: JwtUserDetailsService,
    private val jwtTokenService: JwtTokenService,
    private val jwtRefreshTokenService: JwtRefreshTokenService
) {

    @PostMapping("/authenticate")
    fun authenticate(@RequestBody @Valid authenticationRequest: AuthenticationRequest):
            AuthenticationResponse {
        try {
            jwtAuthenticationManager.authenticate(UsernamePasswordAuthenticationToken(authenticationRequest.email,
                    authenticationRequest.password))
        } catch (ex: BadCredentialsException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        }

        val userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.email!!)
        val authenticationResponse = AuthenticationResponse()
        authenticationResponse.accessToken = jwtTokenService.generateToken(userDetails)
        authenticationResponse.refreshToken =
                jwtRefreshTokenService.generateRefreshToken(userDetails.id!!,
                authenticationRequest.rememberMe!!)
        return authenticationResponse
    }

    @PostMapping("/refresh")
    fun refresh(@RequestBody @Valid refreshTokenRequest: RefreshTokenRequest):
            AuthenticationResponse {
        val username =
                jwtRefreshTokenService.validateRefreshTokenAndGetUsername(refreshTokenRequest.refreshToken!!)
        val userDetails = jwtUserDetailsService.loadUserByUsername(username)
        val authenticationResponse = AuthenticationResponse()
        authenticationResponse.accessToken = jwtTokenService.generateToken(userDetails)
        authenticationResponse.refreshToken = refreshTokenRequest.refreshToken
        return authenticationResponse
    }

}
