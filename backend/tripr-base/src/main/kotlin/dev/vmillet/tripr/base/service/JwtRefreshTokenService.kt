package dev.vmillet.tripr.base.service

import dev.vmillet.tripr.base.domain.RefreshToken
import dev.vmillet.tripr.base.repos.RefreshTokenRepository
import dev.vmillet.tripr.base.repos.UserRepository
import dev.vmillet.tripr.base.util.InvalidTokenException
import java.time.Duration
import java.time.OffsetDateTime
import java.util.UUID
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
class JwtRefreshTokenService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository
) {

    fun generateRefreshToken(userId: Long, rememberMe: Boolean): String {
        val user = userRepository.findById(userId).orElseThrow()
        val newToken = UUID.randomUUID()
        val refreshToken = RefreshToken()
        refreshToken.token = newToken
        refreshToken.expireAt = OffsetDateTime.now().plus(if (rememberMe) REMEMBER_ME_TOKEN_VALIDITY
                else REFRESH_TOKEN_VALIDITY)
        refreshToken.user = user
        refreshTokenRepository.save(refreshToken)
        return newToken.toString()
    }

    fun validateRefreshTokenAndGetUsername(givenToken: String): String {
        val refreshToken =
                refreshTokenRepository.findByTokenAndExpireAtAfter(UUID.fromString(givenToken),
                OffsetDateTime.now())
        if (refreshToken == null) {
            log.warn("refresh token invalid")
            throw InvalidTokenException()
        }
        return refreshToken.user!!.email!!
    }


    companion object {

        val log: Logger = LoggerFactory.getLogger(JwtRefreshTokenService::class.java)

        val REFRESH_TOKEN_VALIDITY: Duration = Duration.ofMinutes(60)

        val REMEMBER_ME_TOKEN_VALIDITY: Duration = Duration.ofDays(180)

    }

}
