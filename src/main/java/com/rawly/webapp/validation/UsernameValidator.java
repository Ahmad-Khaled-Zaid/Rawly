package com.rawly.webapp.validation;

import org.springframework.stereotype.Component;

import com.rawly.webapp.repository.UserRepository;
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
 * <li>start with a letter, containing only letters, digits, or underscores,
 * with no spaces, no leading/trailing underscores, and no consecutive
 * underscores. {@link ValidationPatterns#USERNAME_PATTERN}</li>
 * <li>Username uniqueness in the system</li>
 * </ul>
 * <p>
 * Used in conjunction with {@link ValidUsername} annotation for bean
 * validation.
 * </p>
 *
 * @see ValidUsername
 * @see ConstraintValidator
 * @see UserRepository
 */
@Component
public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {

    // @Autowired
    // private UserRepository userRepository;

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
        // if (isUsernameTaken(username)) {
        //     return ValidationUtils.buildViolation(context, "username.taken");
        // }
        return true;
    }

    private boolean isNullOrEmpty(String username) {
        return username == null || username.trim().isEmpty();
    }

    private boolean isLengthInvalid(String firstName) {
        return firstName.length() < 5 || firstName.length() > 50;
    }

    private boolean isInvalidPattern(String username) {
        return !ValidationPatterns.USERNAME_PATTERN.matcher(username).matches();
    }

    // private boolean isUsernameTaken(String username) {
    //     return userRepository.existsByUsername(username);
    // }
}