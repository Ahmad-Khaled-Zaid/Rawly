package com.rawly.webapp.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidatorContext;

@Component
public class ValidationUtils {
    private static MessageSource messageSource;

    public ValidationUtils(MessageSource messageSource) {
        ValidationUtils.messageSource = messageSource;
    }

    public static boolean buildViolation(ConstraintValidatorContext context, String messageKey) {
        context.disableDefaultConstraintViolation();
        String message = messageSource.getMessage(
                messageKey,
                null,
                messageKey,
                LocaleContextHolder.getLocale());
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        return false;
    }
}
