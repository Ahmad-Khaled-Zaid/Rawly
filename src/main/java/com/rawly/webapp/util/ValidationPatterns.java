package com.rawly.webapp.util;

import java.util.regex.Pattern;

public final class ValidationPatterns {

    private ValidationPatterns() {
    }

    public static final Pattern FIRST_NAME_PATTERN = Pattern.compile("^[A-Za-z]+( [A-Za-z]+)?$");  // Allows only English letters, optionally with a single space between two words
    public static final Pattern LAST_NAME_PATTERN = Pattern.compile("^[A-Za-z]+( [A-Za-z]+)?$");// Allows only English letters, optionally with a single space between two words
    public static final Pattern USERNAME_PATTERN = Pattern.compile("^(?!.*\\s)(?!.*__)(?!_)(?!.*_$)[A-Za-z][A-Za-z0-9_]*$"); //  Username must start with a letter, containing only letters, digits, or underscores, with no spaces, no leading/trailing underscores, and no consecutive underscores.
    public static final Pattern EMAIL_PATTERN = Pattern.compile("^(?!.*\\.\\.)[A-Za-z0-9](?:[A-Za-z0-9._%+-]{0,63}[A-Za-z0-9])?@[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?(?:\\.[A-Za-z]{2,})+$"); // Validates emails by ensuring no consecutive dots, proper local and domain parts, and allowed characters per RFC standards
    public static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^[0-9]{7,12}$"); //Phone number must contain only digits and be between 7 to 12 numbers long.
    public static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+{}\\[\\]:;<>,.?~\\-=/\\\\|'\"])[A-Za-z\\d!@#$%^&*()_+{}\\[\\]:;<>,.?~\\-=/\\\\|'\"]+$"); //Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character.
}
