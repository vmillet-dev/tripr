package dev.vmillet.tripr.security.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User


/**
 * Extension of Spring Security User class to store additional data.
 */
class JwtUserDetails(
    val id: Long?,
    username: String,
    hash: String?,
    authorities: Collection<GrantedAuthority>
) : User(username, hash, authorities)
