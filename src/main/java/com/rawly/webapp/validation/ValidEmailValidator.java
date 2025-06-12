package com.rawly.webapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.apache.commons.validator.routines.EmailValidator;

import com.rawly.webapp.validation.annotations.ValidEmail;

public class ValidEmailValidator implements ConstraintValidator<ValidEmail, String> {

    private final EmailValidator emailValidator = EmailValidator.getInstance(false, false);

    @Override
    public void initialize(com.rawly.webapp.validation.annotations.ValidEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isBlank()) {
            return false;
        }
        String strictRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        if (!email.matches(strictRegex)) {
            return false;
        }
        return emailValidator.isValid(email);
    }
}