package com.rawly.webapp.validation;

import com.rawly.webapp.util.ValidationPatterns;
import com.rawly.webapp.util.ValidationUtils;
import com.rawly.webapp.validation.annotations.ValidPassword;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for password field, implementing custom validation logic.
 * <p>
 * This class checks if a password:
 * <ul>
 * <li>Is not <code>null</code> or empty</li>
 * <li>Does not have leading or trailing whitespace</li>
 * <li>Meets minimum and maximum length requirements</li>
 * <li>Matches a defined password pattern</li>
 * </ul>
 * <p>
 * Validation error messages are added to the context for each specific failure.
 *
 * @see ValidPassword
 * @see ValidationPatterns#PASSWORD_PATTERN
 * @see ValidationUtils#buildViolation(ConstraintValidatorContext, String)
 */
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    private int min;
    private int max;

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (isNullOrEmpty(password)) {
            return ValidationUtils.buildViolation(context, "password.required");
        }
        if (hasLeadingOrTrailingWhitespace(password)) {
            return ValidationUtils.buildViolation(context, "password.whitespace");
        }
        if (isLengthInvalid(password)) {
            return ValidationUtils.buildViolation(context, "password.size");
        }
        if (isPatternInvalid(password)) {
            return ValidationUtils.buildViolation(context, "password.invalid");
        }
        return true;
    }

    private boolean isNullOrEmpty(String password) {
        return password == null || password.trim().isEmpty();
    }

    private boolean hasLeadingOrTrailingWhitespace(String password) {
        return !password.equals(password.trim());
    }

    private boolean isLengthInvalid(String password) {
        return password.length() < this.min || password.length() > this.max;
    }

    private boolean isPatternInvalid(String password) {
        return !ValidationPatterns.PASSWORD_PATTERN.matcher(password).matches();
    }
}
