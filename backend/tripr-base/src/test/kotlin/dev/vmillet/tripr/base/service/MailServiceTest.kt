package dev.vmillet.tripr.base.service

import dev.vmillet.tripr.base.config.BaseIT
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired


class MailServiceTest : BaseIT() {

    @Autowired
    private lateinit var mailService: MailService

    @Test
    fun sendMail_success() {
        mailService.sendMail("bob@invalid.bootify.io", "my subject", "my body")
        waitForMessages(1)
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .`when`()
                    .get(messagesUrl)
                .then()
                    .body("messages[0].To.Address[0]", Matchers.equalTo("bob@invalid.bootify.io"))
                    .body("messages[0].Subject", Matchers.equalTo("my subject"))
    }

}
