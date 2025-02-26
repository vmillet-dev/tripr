package dev.vmillet.tripr.base.rest

import dev.vmillet.tripr.base.model.PasswordResetCompleteRequest
import dev.vmillet.tripr.base.model.PasswordResetRequest
import dev.vmillet.tripr.base.service.PasswordResetService
import jakarta.validation.Valid
import java.lang.Void
import java.util.UUID
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(
    value = ["/passwordReset"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class PasswordResetResource(
    private val passwordResetService: PasswordResetService
) {

    @PostMapping("/start")
    fun start(@RequestBody @Valid passwordResetRequest: PasswordResetRequest):
            ResponseEntity<Void> {
        passwordResetService.startProcess(passwordResetRequest)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/isValidUid")
    fun isValidUid(@RequestParam("uid") passwordResetUid: UUID): ResponseEntity<Boolean> =
            ResponseEntity.ok(passwordResetService.isValidPasswordResetUid(passwordResetUid))

    @PostMapping("/complete")
    fun complete(@RequestBody @Valid passwordResetCompleteRequest: PasswordResetCompleteRequest):
            ResponseEntity<Void> {
        passwordResetService.completeProcess(passwordResetCompleteRequest)
        return ResponseEntity.ok().build()
    }

}
