package com.rawly.webapp.validation;

import org.springframework.stereotype.Component;

import com.rawly.webapp.util.ValidationPatterns;
import com.rawly.webapp.util.ValidationUtils;
import com.rawly.webapp.validation.annotations.ValidUsername;
import com.rawly.webapp.domain.port.UserPersistencePort;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

/**
 * Validates username according to application requirements.
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
@RequiredArgsConstructor
public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {

    private int min;
    private int max;
    private boolean allowNullIfUpdating;
    private final UserPersistencePort userPersistencePort;

    @Override
    public void initialize(ValidUsername constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
        this.allowNullIfUpdating = constraintAnnotation.allowNullIfUpdating();
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (allowNullIfUpdating && username == null) {
            return true;
        }
        if (isNullOrEmpty(username)) {
            return ValidationUtils.buildViolation(context, "validation.username.required");
        }
        if (isLengthInvalid(username)) {
            return ValidationUtils.buildViolation(context, "validation.username.size");
        }
        if (isInvalidPattern(username)) {
            return ValidationUtils.buildViolation(context, "validation.username.invalid");
        }
        if (isUsernameExist(username)) {
            return ValidationUtils.buildViolation(context, "validation.username.in-use");
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

    private boolean isUsernameExist(String username) {
        return userPersistencePort.existsByUsername(username);
    }
}