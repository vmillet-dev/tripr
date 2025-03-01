package dev.vmillet.tripr.security.service

import dev.vmillet.tripr.core.enums.UserRoles
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class ActuatorUserDetailsService : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        if ("actuator" == username) {
            val authorities = listOf(SimpleGrantedAuthority(UserRoles.ACTUATOR))
            return User.withUsername(username)
                    .password("{bcrypt}\$2a\$10\$FMzmOkkfbApEWxS.4XzCKOR7EbbiwzkPEyGgYh6uQiPxurkpzRMa6")
                    .authorities(authorities)
                    .build()
        }
        throw UsernameNotFoundException("User ${username} not found")
    }

}
