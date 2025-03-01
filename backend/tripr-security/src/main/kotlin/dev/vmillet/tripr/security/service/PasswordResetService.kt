package dev.vmillet.tripr.security.service

import dev.vmillet.tripr.persistence.domain.User
import dev.vmillet.tripr.security.model.request.PasswordResetCompleteRequest
import dev.vmillet.tripr.security.model.request.PasswordResetRequest
import dev.vmillet.tripr.persistence.repository.UserRepository
import dev.vmillet.tripr.core.util.WebUtils
import dev.vmillet.tripr.core.service.MailService
import java.time.OffsetDateTime
import java.util.UUID
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.util.Assert


@Service
class PasswordResetService(
    private val mailService: MailService,
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository
) {

    private fun hasValidRequest(user: User?): Boolean = user != null && user.passwordResetUid !=
            null && user.passwordResetStart!!.plusWeeks(1).isAfter(OffsetDateTime.now())

    fun startProcess(passwordResetRequest: PasswordResetRequest) {
        log.info("received password reset request for {}", passwordResetRequest.email)

        val user = userRepository.findByEmailIgnoreCase(passwordResetRequest.email!!)
        if (user == null) {
            log.warn("user {} not found", passwordResetRequest.email)
            return
        }

        // keep existing uid if still valid
        if (!hasValidRequest(user)) {
            user.passwordResetUid = UUID.randomUUID()
        }
        user.passwordResetStart = OffsetDateTime.now()
        userRepository.save(user)

        mailService.sendMail(passwordResetRequest.email!!,
                WebUtils.getMessage("passwordReset.mail.subject")!!,
                WebUtils.renderTemplate("/mails/passwordReset", mapOf("passwordResetUid" to
                user.passwordResetUid)))
    }

    fun isValidPasswordResetUid(passwordResetUid: UUID): Boolean {
        val user = userRepository.findByPasswordResetUid(passwordResetUid)
        if (hasValidRequest(user)) {
            return true
        }
        log.warn("invalid password reset uid {}", passwordResetUid)
        return false
    }

    fun completeProcess(passwordResetCompleteRequest: PasswordResetCompleteRequest) {
        val user = userRepository.findByPasswordResetUid(passwordResetCompleteRequest.uid!!)
        Assert.isTrue(hasValidRequest(user), "invalid update password request")

        log.warn("updating password for user {}", user!!.email)

        user.password = passwordEncoder.encode(passwordResetCompleteRequest.newPassword)
        user.passwordResetUid = null
        user.passwordResetStart = null
        userRepository.save(user)
    }


    companion object {

        val log: Logger = LoggerFactory.getLogger(PasswordResetService::class.java)

    }

}
