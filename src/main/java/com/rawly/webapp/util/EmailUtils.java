package com.rawly.webapp.util;

import org.apache.commons.validator.routines.EmailValidator;

public class EmailUtils {

    private static final String STRICT_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final EmailValidator emailValidator = EmailValidator.getInstance(false, false);

    public static boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }

        if (!email.matches(STRICT_REGEX)) {
            return false;
        }
        return emailValidator.isValid(email);
    }
}
