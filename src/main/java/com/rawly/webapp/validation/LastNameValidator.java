package com.rawly.webapp.validation;

import com.rawly.webapp.util.ValidationPatterns;
import com.rawly.webapp.util.ValidationUtils;
import com.rawly.webapp.validation.annotations.ValidLastName;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for last name field.
 * Implements {@link ConstraintValidator} for the {@link ValidLastName}
 * annotation.
 * 
 * This validator ensures that a last name:
 * <ul>
 * <li>Must not be {@code null} or empty</li>
 * <li>Must not contain leading or trailing whitespace</li>
 * <li>Length must be between 3 and 50 characters</li>
 * <li>Must contain only English alphabet letters (A–Z, a–z)</li>
 * <li>If the name is compound (e.g., two words), it may contain a single space
 * between the words</li>
 * </ul>
 * Example valid names: <code>Smith</code>, <code>Van Damme</code>
 * Example invalid names: <code>&#x2423;John&#x2423;</code>, <code>Al3x</code>,
 * <code>Al--Ali</code>
 *
 * @see ValidLastName
 * @see ConstraintValidator
 */

public class LastNameValidator implements ConstraintValidator<ValidLastName, String> {

    private int min;
    private int max;
    private boolean allowNullIfUpdating;

    @Override
    public void initialize(ValidLastName constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
        this.allowNullIfUpdating = constraintAnnotation.allowNullIfUpdating();
    }

    @Override
    public boolean isValid(String lastName, ConstraintValidatorContext context) {
        if (allowNullIfUpdating && lastName == null) {
            return true;
        }
        if (isNullOrEmpty(lastName)) {
            return ValidationUtils.buildViolation(context, "validation.last-name.required");
        }
        if (hasLeadingOrTrailingWhitespace(lastName)) {
            return ValidationUtils.buildViolation(context, "validation.last-name.whitespace");
        }
        if (isLengthOutOfBounds(lastName)) {
            return ValidationUtils.buildViolation(context, "validation.last-name.size");
        }
        if (isInvalidPattern(lastName)) {
            return ValidationUtils.buildViolation(context, "validation.last-name.invalid");
        }
        return true;
    }

    private boolean isNullOrEmpty(String lastName) {
        return lastName == null || lastName.trim().isEmpty();
    }

    private boolean hasLeadingOrTrailingWhitespace(String lastName) {
        return !lastName.equals(lastName.trim());
    }

    private boolean isLengthOutOfBounds(String lastName) {
        return lastName.length() < this.min || lastName.length() > this.max;
    }

    private boolean isInvalidPattern(String lastName) {
        return !ValidationPatterns.LAST_NAME_PATTERN.matcher(lastName).matches();
    }
}
