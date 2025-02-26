package dev.vmillet.tripr.base.config

import dev.vmillet.tripr.base.TriprApplication
import dev.vmillet.tripr.base.repos.RefreshTokenRepository
import dev.vmillet.tripr.base.repos.RoleRepository
import dev.vmillet.tripr.base.repos.UserRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import jakarta.annotation.PostConstruct
import java.lang.RuntimeException
import java.lang.Thread
import java.nio.charset.StandardCharsets
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlMergeMode
import org.springframework.util.StreamUtils
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait


/**
 * Abstract base class to be extended by every IT test. Starts the Spring Boot context with a
 * Datasource connected to the Testcontainers Docker instance. The instance is reused for all tests,
 * with all data wiped out before each test.
 */
@SpringBootTest(
    classes = [TriprApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("it")
@Sql(
    "/data/clearAll.sql",
    "/data/roleData.sql",
    "/data/userData.sql"
)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
abstract class BaseIT {

    @LocalServerPort
    var serverPort = 0

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var refreshTokenRepository: RefreshTokenRepository

    @Autowired
    lateinit var roleRepository: RoleRepository

    @PostConstruct
    fun initRestAssured() {
        RestAssured.port = serverPort
        RestAssured.urlEncodingEnabled = false
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @BeforeEach
    fun beforeEach() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .`when`()
                    .delete(messagesUrl)
    }

    fun readResource(resourceName: String): String =
            StreamUtils.copyToString(this.javaClass.getResourceAsStream(resourceName),
            StandardCharsets.UTF_8)

    fun waitForMessages(total: Int) {
        var loop = 0
        while (loop++ < 25) {
            val messagesResponse = RestAssured
                    .given()
                        .accept(ContentType.JSON)
                    .`when`()
                        .get(messagesUrl)
            if (messagesResponse.jsonPath().getInt("total") == total) {
                return
            }
            Thread.sleep(250)
        }
        throw RuntimeException("Could not find ${total} messages in time.")
    }

    fun adminJwtToken(): String {
        // user admin@invalid.bootify.io, expires 2040-01-01
        return "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9." +
                "eyJzdWIiOiJhZG1pbkBpbnZhbGlkLmJvb3RpZnkuaW8iLCJyb2xlcyI6WyJBRE1JTiJdLCJpc3MiOiJib290aWZ5IiwiaWF0IjoxNzQwMTU2Njk5LCJleHAiOjIyMDg5ODg4MDB9." +
                "e4EUQqneoYUTHW29rXxA4DNpud-1tjOLEBSGVQNFT5BTmJVIYB0F7zETwGV-bnd2TqrLzbIzI6UCNxQa-NjcuQ"
    }

    fun memberJwtToken(): String {
        // user member@invalid.bootify.io, expires 2040-01-01
        return "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9." +
                "eyJzdWIiOiJtZW1iZXJAaW52YWxpZC5ib290aWZ5LmlvIiwicm9sZXMiOlsiTUVNQkVSIl0sImlzcyI6ImJvb3RpZnkiLCJpYXQiOjE3NDAxNTY2OTksImV4cCI6MjIwODk4ODgwMH0." +
                "BuXGTnzkwMwps0ZIsMLp3zZQrn2v11shrj8cbsdrZxY3TFgoZCbmlaWPobUuqsG_TiLtPxJbMn5PuMoiCok5MQ"
    }


    companion object {

        @ServiceConnection
        val postgreSQLContainer = PostgreSQLContainer("postgres:17.3")

        val mailpitContainer = GenericContainer("axllent/mailpit:v1.22")

        var smtpHost: String

        var smtpPort: Int

        var messagesUrl: String

        const val ACTUATOR = "actuator"

        const val PASSWORD = "Bootify!"

        init {
            postgreSQLContainer.withReuse(true)
                    .start()
            mailpitContainer.withExposedPorts(1025, 8025)
                    .waitingFor(Wait.forLogMessage(".*accessible via.*", 1))
                    .withReuse(true)
                    .start()
            smtpHost = mailpitContainer.host
            smtpPort = mailpitContainer.getMappedPort(1025)
            messagesUrl = "http://" + smtpHost + ":" + mailpitContainer.getMappedPort(8025) +
                    "/api/v1/messages"
        }

        @DynamicPropertySource
        @JvmStatic
        fun setDynamicProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.mail.host") { smtpHost }
            registry.add("spring.mail.port") { smtpPort }
            registry.add("spring.mail.properties.mail.smtp.auth") { false }
            registry.add("spring.mail.properties.mail.smtp.starttls.enable") { false }
            registry.add("spring.mail.properties.mail.smtp.starttls.required") { false }
            registry.add("jwt.secret") { "D1A4798A74E1310E0B89079E66FC826E" }
            registry.add("app.baseHost") { "http://localhost:8080" }
        }

    }

}
