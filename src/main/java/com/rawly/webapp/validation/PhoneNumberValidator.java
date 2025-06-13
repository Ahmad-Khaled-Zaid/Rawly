package com.rawly.webapp.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rawly.webapp.repository.UserRepository;
import com.rawly.webapp.validation.annotations.ValidPhoneNumber;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    @Autowired
    private UserRepository userRepository;
    private static final String PHONE_NUMBER_PATTERN = "^[0-9]{7,12}$";

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }

        if (!phoneNumber.matches(PHONE_NUMBER_PATTERN)) {
            return false;
        }

        return !userRepository.existsByPhoneNumber(phoneNumber);
    }
}