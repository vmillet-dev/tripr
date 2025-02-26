package dev.vmillet.tripr.base.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
class MailProperties {

    @Value("\${app.mail.from}")
    var mailFrom: String? = null

}
