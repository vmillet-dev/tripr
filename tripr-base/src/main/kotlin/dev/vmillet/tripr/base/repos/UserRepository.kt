package dev.vmillet.tripr.base.repos

import dev.vmillet.tripr.base.domain.User
import java.util.UUID
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository


interface UserRepository : JpaRepository<User, Long> {

    @EntityGraph(attributePaths = ["role"])
    fun findByEmailIgnoreCase(email: String): User?

    fun findByPasswordResetUid(passwordResetUid: UUID): User?

    fun existsByEmailIgnoreCase(email: String?): Boolean

}
