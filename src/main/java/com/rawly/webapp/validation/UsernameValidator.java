package com.rawly.webapp.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rawly.webapp.repository.UserRepository;
import com.rawly.webapp.validation.annotations.ValidUsername;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for username fields.
 * Implements {@link ConstraintValidator} for the {@link ValidUsername}
 * annotation.
 * 
 * The validator enforces:
 * <ul>
 *   <li>Length between 5 and 50 characters</li>
 *   <li>Start with a letter (A-Z, a-z)</li>
 *   <li>Contains only letters, digits, or underscores</li>
 *   <li>No leading or trailing underscores</li>
 *   <li>No consecutive underscores</li>
 *   <li>No '@' symbol allowed</li>
 *   <li>Must be unique (not already taken by another user)</li>
 * </ul>
 * 
 * @see ValidUsername
 * @see ConstraintValidator
 */
@Component
public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {

    @Autowired
    private UserRepository userRepository;
    private static final String USERNAME_PATTERN = "^(?!.*@)(?!.*__)(?!_)(?![0-9])[A-Za-z0-9_]{5,50}(?<!_)$";

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }

        if (!username.matches(USERNAME_PATTERN)) {
            return false;
        }

        return !userRepository.existsByUsername(username);
    }
}