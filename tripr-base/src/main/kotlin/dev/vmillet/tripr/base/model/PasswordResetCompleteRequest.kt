package dev.vmillet.tripr.base.model

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.util.UUID


class PasswordResetCompleteRequest {

    @NotNull
    var uid: UUID? = null

    @NotNull
    @Size(max = 255)
    var newPassword: String? = null

}
