package dev.vmillet.tripr.base.model

import dev.vmillet.tripr.base.repos.UserRepository
import dev.vmillet.tripr.base.util.WebUtils
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass


/**
 * Validate that there is an account for the given e-mail.
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER,
        AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [PasswordResetRequestEmailExistsValidator::class])
annotation class PasswordResetRequestEmailExists(
    val message: String = "{passwordReset.start.noAccount}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)


class PasswordResetRequestEmailExistsValidator(
    private val userRepository: UserRepository
) : ConstraintValidator<PasswordResetRequestEmailExists, String> {

    override fun isValid(`value`: String?, cvContext: ConstraintValidatorContext): Boolean {
        if (value == null || !value.matches(WebUtils.EMAIL_PATTERN.toRegex())) {
            // no valid value present
            return true
        }
        return userRepository.existsByEmailIgnoreCase(value)
    }

}
