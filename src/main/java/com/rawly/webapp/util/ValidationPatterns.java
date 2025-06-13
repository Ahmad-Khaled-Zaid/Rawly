package com.rawly.webapp.util;

import java.util.regex.Pattern;

public final class ValidationPatterns {

    private ValidationPatterns() {
    }

    public static final Pattern FIRST_NAME_PATTERN = Pattern.compile("^[A-Za-z]+( [A-Za-z]+)?$");  // Allows only English letters, optionally with a single space between two words
    public static final Pattern LAST_NAME_PATTERN = Pattern.compile("^[A-Za-z]+( [A-Za-z]+)?$");// Allows only English letters, optionally with a single space between two words
}
