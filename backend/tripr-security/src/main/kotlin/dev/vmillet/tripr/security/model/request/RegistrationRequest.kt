package dev.vmillet.tripr.security.model.request

import dev.vmillet.tripr.core.util.WebUtils
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size


class RegistrationRequest {

    @NotNull
    @Size(max = 255)
    @Email(regexp = WebUtils.EMAIL_PATTERN)
    @RegistrationRequestEmailUnique
    var email: String? = null

    @NotNull
    @Size(max = 72)
    var password: String? = null

}
