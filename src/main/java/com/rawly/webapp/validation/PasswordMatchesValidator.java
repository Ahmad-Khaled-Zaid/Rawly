package com.rawly.webapp.validation;

import com.rawly.webapp.dto.auth.RegisterRequest;
import com.rawly.webapp.validation.annotations.PasswordMatches;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegisterRequest> {

    @Override
    public boolean isValid(RegisterRequest request, ConstraintValidatorContext context) {
        return request.getPassword() != null && request.getPassword().equals(request.getConfirmedPassword());

    }
}
