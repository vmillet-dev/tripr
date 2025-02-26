package dev.vmillet.tripr.base.repos

import dev.vmillet.tripr.base.domain.Role
import org.springframework.data.jpa.repository.JpaRepository


interface RoleRepository : JpaRepository<Role, Long> {

    fun findByName(name: String): Role

}
