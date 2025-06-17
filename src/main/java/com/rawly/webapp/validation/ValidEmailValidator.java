package com.rawly.webapp.validation;

import org.apache.commons.validator.routines.EmailValidator;

import com.rawly.webapp.util.ValidationUtils;
import com.rawly.webapp.validation.annotations.ValidEmail;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidEmailValidator implements ConstraintValidator<ValidEmail, String> {

    private final EmailValidator emailValidator = EmailValidator.getInstance(false, true);

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (isNullOrEmpty(email)) {
            return ValidationUtils.buildViolation(context, "email.required");
        }

        if (isInvalidEmail(email)) {
            return ValidationUtils.buildViolation(context, "email.invalid");
        }
        // if (isEmailTaken(email)) {
        //     return ValidationUtils.buildViolation(context, "email.taken");
        // }

        // if (isInvalidPattern(email)) {
        // return ValidationUtils.buildViolation(context, "email.invalid");
        // }
        return true;
    }

    private boolean isNullOrEmpty(String email) {
        return email == null || email.trim().isEmpty();
    }

    private boolean isInvalidEmail(String email) {
        return !emailValidator.isValid(email);
    }


    // private boolean isInvalidPattern(String email) {
    //     return !ValidationPatterns.EMAIL_PATTERN.matcher(email).matches();
    // }
}