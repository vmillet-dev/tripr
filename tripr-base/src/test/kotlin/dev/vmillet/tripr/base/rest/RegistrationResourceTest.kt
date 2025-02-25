package dev.vmillet.tripr.base.rest

import dev.vmillet.tripr.base.config.BaseIT
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus


class RegistrationResourceTest : BaseIT() {

    @Test
    fun register_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/registrationRequest.json"))
                .`when`()
                    .post("/register")
                .then()
                    .statusCode(HttpStatus.OK.value())
            Assertions.assertEquals(3, userRepository.count())
        }

    }
