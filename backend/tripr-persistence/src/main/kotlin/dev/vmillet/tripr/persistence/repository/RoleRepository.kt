package dev.vmillet.tripr.persistence.repository

import dev.vmillet.tripr.persistence.domain.Role
import org.springframework.data.jpa.repository.JpaRepository


interface RoleRepository : JpaRepository<Role, Long> {

    fun findByName(name: String): Role

}
