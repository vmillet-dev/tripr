package dev.vmillet.tripr.core.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service


@Service
open class MailService(
    private val javaMailSender: JavaMailSender,
    @Value("\${app.mail.from}") val mailFrom: String,
) {

    @Async
    open fun sendMail(
        mailTo: String,
        subject: String,
        html: String
    ) {
        log.info("sending mail {} to {}", subject, mailTo)

        javaMailSender.send { mimeMessage ->
            val message = MimeMessageHelper(mimeMessage, true, "UTF-8")
            message.setFrom(mailFrom)
            message.setTo(mailTo)
            message.setSubject(subject)
            message.setText(html, true)
        }

        log.info("sending completed")
    }


    companion object {

        val log: Logger = LoggerFactory.getLogger(MailService::class.java)

    }

}
