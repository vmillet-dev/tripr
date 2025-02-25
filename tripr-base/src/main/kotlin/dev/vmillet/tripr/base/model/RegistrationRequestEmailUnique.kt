package dev.vmillet.tripr.base.model

import dev.vmillet.tripr.base.service.RegistrationService
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass


/**
 * Validate that the email value isn't taken yet.
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER,
        AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [RegistrationRequestEmailUniqueValidator::class])
annotation class RegistrationRequestEmailUnique(
    val message: String = "{registration.register.taken}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)


class RegistrationRequestEmailUniqueValidator(
    private val registrationService: RegistrationService
) : ConstraintValidator<RegistrationRequestEmailUnique, String> {

    override fun isValid(`value`: String?, cvContext: ConstraintValidatorContext): Boolean {
        if (value == null) {
            // no value present
            return true
        }
        return !registrationService.emailExists(value)
    }

}
