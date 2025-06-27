package com.rawly.webapp.validation;

import com.rawly.webapp.util.ValidationPatterns;
import com.rawly.webapp.util.ValidationUtils;
import com.rawly.webapp.validation.annotations.ValidFirstName;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for first name field.
 * <p>
 * Implements {@link ConstraintValidator} for the {@link ValidFirstName}
 * annotation.
 * </p>
 *
 * This validator enforces the following rules on first name input:
 * <ul>
 * <li>Must not be {@code null} or empty</li>
 * <li>Must not contain leading or trailing whitespace</li>
 * <li>Length must be between 3 and 50 characters</li>
 * <li>Must contain only English alphabet letters (A–Z, a–z)</li>
 * <li>If the name is compound (e.g., two words), it may contain a single space
 * between the words</li>
 * </ul>
 *
 * <p>
 * Example valid names: <code>John</code>, <code>Mary Jane</code><br>
 * Example invalid names: <code>&#x2423;Alaa&#x2423;</code>, <code>Jo3n</code>,
 * <code>Ali&#x2423;&#x2423;Omar</code>
 * </p>
 *
 * @see ValidFirstName
 * @see ConstraintValidator
 */
public class FirstNameValidator implements ConstraintValidator<ValidFirstName, String> {

    private int min;
    private int max;
    private boolean isUpdate;

    @Override
    public void initialize(ValidFirstName constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
        this.isUpdate = constraintAnnotation.isUpdate();
    }

    @Override
    public boolean isValid(String firstName, ConstraintValidatorContext context) {
        if (isUpdate && firstName == null) {
            return true;
        }
        if (isNullOrEmpty(firstName)) {
            return ValidationUtils.buildViolation(context, "first.name.required");
        }
        if (hasLeadingOrTrailingWhitespace(firstName)) {
            return ValidationUtils.buildViolation(context, "first.name.whitespace");
        }
        if (isLengthOutOfBounds(firstName)) {
            return ValidationUtils.buildViolation(context, "first.name.size");
        }
        if (isInvalidPattern(firstName)) {
            return ValidationUtils.buildViolation(context, "first.name.invalid");
        }

        return true;
    }

    private boolean isNullOrEmpty(String firstName) {
        return firstName == null || firstName.trim().isEmpty();
    }

    private boolean hasLeadingOrTrailingWhitespace(String firstName) {
        return !firstName.equals(firstName.trim());
    }

    private boolean isLengthOutOfBounds(String firstName) {
        return firstName.length() < this.min || firstName.length() > this.max;
    }

    private boolean isInvalidPattern(String firstName) {
        return !ValidationPatterns.FIRST_NAME_PATTERN.matcher(firstName).matches();
    }

}
