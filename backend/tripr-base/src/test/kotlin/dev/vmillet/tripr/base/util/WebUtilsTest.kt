package dev.vmillet.tripr.base.util

import dev.vmillet.tripr.core.util.WebUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class WebUtilsTest {

    @Test
    fun emailValidation() {
        val validEmails = listOf("test@domain.com", "valid-email+1@invalid.bootify.io",
                "A1_2@TEST-1-2.TECHNOLOGY")
        val invalidEmails = listOf("invalid@domain", "invalid.domain", "test-@domain.com",
                "test#test@domain.com", "test--test@domain.com")
        for (validEmail in validEmails) {
            Assertions.assertTrue(validEmail.matches(WebUtils.EMAIL_PATTERN.toRegex()),
                    "email is not valid: $validEmail")
        }
        for (invalidEmail in invalidEmails) {
            Assertions.assertFalse(invalidEmail.matches(WebUtils.EMAIL_PATTERN.toRegex()),
                    "email is not invalid: $invalidEmail")
        }
    }

}
