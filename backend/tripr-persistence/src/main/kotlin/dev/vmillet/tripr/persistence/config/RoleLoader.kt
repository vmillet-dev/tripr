package dev.vmillet.tripr.persistence.config

import dev.vmillet.tripr.persistence.domain.Role
import dev.vmillet.tripr.persistence.repository.RoleRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component


@Component
class RoleLoader(
    private val roleRepository: RoleRepository
) : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        if (roleRepository.count() != 0L) {
            return
        }
        log.info("initializing roles")
        val adminRole = Role()
        adminRole.name = "ADMIN"
        roleRepository.save(adminRole)
        val memberRole = Role()
        memberRole.name = "MEMBER"
        roleRepository.save(memberRole)
    }


    companion object {

        val log: Logger = LoggerFactory.getLogger(RoleLoader::class.java)

    }

}
