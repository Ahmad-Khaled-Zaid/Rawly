package com.rawly.webapp.validation;

import com.rawly.webapp.util.ValidationPatterns;
import com.rawly.webapp.util.ValidationUtils;
import com.rawly.webapp.validation.annotations.ValidUsername;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validates usernames according to application requirements.
 * This validator checks for:
 * <ul>
 * <li>Must not be {@code null} or empty</li>
 * <li>Length must be between 5 and 50 characters</li>
 * <li>Start with a letter, containing only letters, digits, or underscores,
 * with no spaces, no leading/trailing underscores, and no consecutive
 * underscores. {@link ValidationPatterns#USERNAME_PATTERN}</li>
 * </ul>
 * <p>
 * Used in conjunction with {@link ValidUsername} annotation for bean
 * validation.
 * </p>
 *
 * @see ValidUsername
 * @see ConstraintValidator
 */
public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {

    private int min;
    private int max;

    @Override
    public void initialize(ValidUsername constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (isNullOrEmpty(username)) {
            return ValidationUtils.buildViolation(context, "username.required");
        }
        if (isLengthInvalid(username)) {
            return ValidationUtils.buildViolation(context, "username.size");
        }
        if (isInvalidPattern(username)) {
            return ValidationUtils.buildViolation(context, "username.invalid");
        }

        return true;
    }

    private boolean isNullOrEmpty(String username) {
        return username == null || username.trim().isEmpty();
    }

    private boolean isLengthInvalid(String username) {
        return username.length() < this.min || username.length() > this.max;
    }

    private boolean isInvalidPattern(String username) {
        return !ValidationPatterns.USERNAME_PATTERN.matcher(username).matches();
    }
}