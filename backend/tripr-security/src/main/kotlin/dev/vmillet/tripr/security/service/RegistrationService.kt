package dev.vmillet.tripr.security.service

import dev.vmillet.tripr.persistence.domain.User
import dev.vmillet.tripr.security.model.request.RegistrationRequest
import dev.vmillet.tripr.persistence.repository.RoleRepository
import dev.vmillet.tripr.persistence.repository.UserRepository
import dev.vmillet.tripr.core.enums.UserRoles
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class RegistrationService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val roleRepository: RoleRepository
) {

    fun register(registrationRequest: RegistrationRequest) {
        log.info("registering new user: {}", registrationRequest.email)

        val user = User()
        user.email = registrationRequest.email
        user.password = passwordEncoder.encode(registrationRequest.password)
        // assign default role
        user.role = roleRepository.findByName(UserRoles.ADMIN)
        userRepository.save(user)
    }

    fun emailExists(email: String?): Boolean = userRepository.existsByEmailIgnoreCase(email)


    companion object {

        val log: Logger = LoggerFactory.getLogger(RegistrationService::class.java)

    }

}
