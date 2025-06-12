package com.rawly.webapp.utilities;

import org.apache.commons.validator.routines.EmailValidator;

public class EmailUtils {
    private static final EmailValidator validator = EmailValidator.getInstance(false, false);

    public static boolean isValidEmail(String email) {
        return validator.isValid(email);
    }
}
