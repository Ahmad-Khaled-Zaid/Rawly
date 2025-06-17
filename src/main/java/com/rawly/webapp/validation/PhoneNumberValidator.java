package com.rawly.webapp.validation;

import org.springframework.stereotype.Component;

import com.rawly.webapp.util.ValidationPatterns;
import com.rawly.webapp.util.ValidationUtils;
import com.rawly.webapp.validation.annotations.ValidPhoneNumber;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    // @Autowired
    // private UserRepository userRepository;

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {

        if (isNullOrEmpty(phoneNumber)) {
            return ValidationUtils.buildViolation(context, "phone.number.required");
        }
        if (isInvalidPattern(phoneNumber)) {
            return ValidationUtils.buildViolation(context, "phone.number.invalid");
        }
        // if (isPhoneNumberTaken(phoneNumber)) {
        //     return ValidationUtils.buildViolation(context, "phone.number.taken");
        // }
        return true;
    }

    private boolean isNullOrEmpty(String phoneNumber) {
        return (phoneNumber == null || phoneNumber.trim().isEmpty());
    }

    private boolean isInvalidPattern(String phoneNumber) {
        return !ValidationPatterns.PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
    }

    // private boolean isPhoneNumberTaken(String phoneNumber) {
    //     return userRepository.existsByPhoneNumber(phoneNumber);
    // }
}
