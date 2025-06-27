package com.rawly.webapp.validation;

import com.rawly.webapp.util.ValidationPatterns;
import com.rawly.webapp.util.ValidationUtils;
import com.rawly.webapp.validation.annotations.ValidPhoneNumber;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for phone number, implementing the {@link ConstraintValidator}
 * interface.
 * <p>
 * This class checks if a given phone number string is valid according to the
 * following rules:
 * <ul>
 * <li>The phone number must not be <code>null</code> or empty.</li>
 * <li>The phone number must match the pattern defined in
 * {@link ValidationPatterns#PHONE_NUMBER_PATTERN}.</li>
 * </ul>
 * If validation fails, a violation message is added to the context using
 * {@link ValidationUtils#buildViolation}.
 * </p>
 */
public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    private boolean isUpdate;

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
        this.isUpdate = constraintAnnotation.isUpdate();
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (isUpdate && phoneNumber == null) {
            return true;
        }

        if (isNullOrEmpty(phoneNumber)) {
            return ValidationUtils.buildViolation(context, "phone.number.required");
        }
        if (isInvalidPattern(phoneNumber)) {
            return ValidationUtils.buildViolation(context, "phone.number.invalid");
        }
        return true;
    }

    private boolean isNullOrEmpty(String phoneNumber) {
        return (phoneNumber == null || phoneNumber.trim().isEmpty());
    }

    private boolean isInvalidPattern(String phoneNumber) {
        return !ValidationPatterns.PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
    }

}
