package dev.vmillet.tripr.persistence.repository

import dev.vmillet.tripr.persistence.domain.RefreshToken
import java.time.OffsetDateTime
import java.util.UUID
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository


interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {

    @EntityGraph(attributePaths = ["user"])
    fun findByTokenAndExpireAtAfter(token: UUID, expireAt: OffsetDateTime): RefreshToken?

}
