package dev.vmillet.tripr.base.model

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size


class RefreshTokenRequest {

    @NotNull
    @Size(max = 255)
    var refreshToken: String? = null

}
