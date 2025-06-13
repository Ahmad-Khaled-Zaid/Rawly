package com.rawly.webapp.util;

import jakarta.validation.ConstraintValidatorContext;

public class ValidationUtils {
    private ValidationUtils() {
        // private constructor to prevent instantiation
    }

    public static boolean buildViolation(ConstraintValidatorContext context, String messageKey) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("{" + messageKey + "}").addConstraintViolation();
        return false;
    }
}
