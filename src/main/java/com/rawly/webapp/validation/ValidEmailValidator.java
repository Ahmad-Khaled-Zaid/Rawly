package com.rawly.webapp.validation;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

import com.rawly.webapp.util.ValidationPatterns;
import com.rawly.webapp.util.ValidationUtils;
import com.rawly.webapp.validation.annotations.ValidEmail;
import com.rawly.webapp.domain.port.UserPersistencePort;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

/**
 * Validator for email field annotated with {@link ValidEmail}.
 * <p>
 * This class validates that the email:
 * <ul>
 * <li>Is not <code>null</code> or empty</li>
 * <li>Follows general email formatting rules using Apache Commons
 * Validator</li>
 * <li>Matches the application's custom email regex pattern defined in
 * {@link ValidationPatterns#EMAIL_PATTERN}</li>
 * </ul>
 * <p>
 * It is used by the Jakarta Bean Validation framework to validate fields
 * annotated with {@code @ValidEmail}.
 * </p>
 *
 * @see ValidEmail
 * @see ConstraintValidator
 * @see ValidationUtils
 * @see ValidationPatterns
 */
@RequiredArgsConstructor
@Component
public class ValidEmailValidator implements ConstraintValidator<ValidEmail, String> {

    private final EmailValidator emailValidator = EmailValidator.getInstance(false, true);
    private final UserPersistencePort userRepositoryPort;
    private boolean allowNullIfUpdating;

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        this.allowNullIfUpdating = constraintAnnotation.allowNullIfUpdating();
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (allowNullIfUpdating && email == null) {
            return true;
        }
        if (isNullOrEmpty(email)) {
            return ValidationUtils.buildViolation(context, "validation.email.required");
        }

        if (isInvalidEmail(email)) {
            return ValidationUtils.buildViolation(context, "validation.email.invalid");
        }

        if (isInvalidPattern(email)) {
            return ValidationUtils.buildViolation(context, "validation.email.invalid");
        }
        if (isEmailRegistered(email)) {
            return ValidationUtils.buildViolation(context, "validation.email.in-use");
        }
        return true;
    }

    private boolean isNullOrEmpty(String email) {
        return email == null || email.trim().isEmpty();
    }

    private boolean isInvalidEmail(String email) {
        return !emailValidator.isValid(email);
    }

    private boolean isInvalidPattern(String email) {
        return !ValidationPatterns.EMAIL_PATTERN.matcher(email).matches();
    }

    private boolean isEmailRegistered(String email) {
        return userRepositoryPort.existsByEmail(email);
    }
}