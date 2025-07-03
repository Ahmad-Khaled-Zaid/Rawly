package com.rawly.webapp.validation;

import org.springframework.stereotype.Component;

import com.rawly.webapp.util.ValidationPatterns;
import com.rawly.webapp.util.ValidationUtils;
import com.rawly.webapp.validation.annotations.ValidPhoneNumber;
import com.rawly.webapp.domain.port.UserPersistencePort;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor
public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    private boolean allowNullIfUpdating;
    private final UserPersistencePort userRepositoryPort;

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
        this.allowNullIfUpdating = constraintAnnotation.allowNullIfUpdating();
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (allowNullIfUpdating && phoneNumber == null) {
            return true;
        }

        if (isNullOrEmpty(phoneNumber)) {
            return ValidationUtils.buildViolation(context, "validation.phone-number.invalid");
        }
        if (isInvalidPattern(phoneNumber)) {
            return ValidationUtils.buildViolation(context, "validation.phone-number.invalid");
        }
        if (isPhoneNumberExist(phoneNumber)) {
            return ValidationUtils.buildViolation(context, "validation.phone-number.in-use");
        }
        return true;
    }

    private boolean isNullOrEmpty(String phoneNumber) {
        return (phoneNumber == null || phoneNumber.trim().isEmpty());
    }

    private boolean isInvalidPattern(String phoneNumber) {
        return !ValidationPatterns.PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
    }

    private boolean isPhoneNumberExist(String phoneNumber) {
        return userRepositoryPort.existsByPhoneNumber(phoneNumber);
    }

}
