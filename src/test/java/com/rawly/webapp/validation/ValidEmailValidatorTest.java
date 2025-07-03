package com.rawly.webapp.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintValidatorContext;

public class ValidEmailValidatorTest {

    private ValidEmailValidator validator;
    private ConstraintValidatorContext context;

    // @BeforeEach
    // void setUp() {
    //     validator = new ValidEmailValidator();
    //     context = mock(ConstraintValidatorContext.class);
    //     ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(
    //             ConstraintValidatorContext.ConstraintViolationBuilder.class);
    //     when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
    //     when(builder.addConstraintViolation()).thenReturn(context);

    // }

    @Test
    void testNullEmail() {
        assertFalse(validator.isValid(null, context));
    }

    @Test
    void testEmptyEmail() {
        assertFalse(validator.isValid("", context));
    }

    @Test
    void testWhitespaceEmail() {
        assertFalse(validator.isValid("   ", context));
    }

    @Test
    void testEmailWithoutAtSymbol() {
        assertFalse(validator.isValid("user.example.com", context));
    }

    @Test
    void testEmailWithMultipleAtSymbols() {
        assertFalse(validator.isValid("user@@example.com", context));
    }

    @Test
    void testEmailWithoutDomain() {
        assertFalse(validator.isValid("user@", context));
    }

    @Test
    void testEmailWithoutUsername() {
        assertFalse(validator.isValid("@example.com", context));
    }

    @Test
    void testEmailWithInvalidDomain() {
        assertFalse(validator.isValid("user@example", context));
        assertFalse(validator.isValid("user@example.", context));
        assertFalse(validator.isValid("user@.com", context));
    }

    @Test
    void testEmailWithSpaces() {
        assertFalse(validator.isValid("user @example.com", context));
        assertFalse(validator.isValid("user@ example.com", context));
        assertFalse(validator.isValid("user@example. com", context));
    }

    @Test
    void testEmailWithSpecialCharacters() {
        assertFalse(validator.isValid("user<>@example.com", context));
        assertFalse(validator.isValid("user()@example.com", context));
        assertFalse(validator.isValid("user[]@example.com", context));
    }

    @Test
    void testValidEmails() {
        assertTrue(validator.isValid("user@example.com", context));
        assertTrue(validator.isValid("user.name@example.com", context));
        assertTrue(validator.isValid("user_name@example.co.uk", context));
        assertTrue(validator.isValid("user-name+tag@example.io", context));
        assertTrue(validator.isValid("u.ser+name@sub.example.com", context));
    }

    @Test
    void testEmailWithUnicodeCharactersShouldFail() {
        assertFalse(validator.isValid("usér@example.com", context));
        assertFalse(validator.isValid("用户@例子.公司", context));
    }

    @Test
    void testEmailWithConsecutiveDots() {
        assertFalse(validator.isValid("user..name@example.com", context));
        assertFalse(validator.isValid("user@exa..mple.com", context));
    }

    @Test
    void testEmailWithLeadingOrTrailingDot() {
        assertFalse(validator.isValid(".user@example.com", context));
        assertFalse(validator.isValid("user.@example.com", context));
    }

    @Test
    void testEmailWithLongTLD() {
        assertTrue(validator.isValid("user@example.technology", context));
    }

    @Test
    void testEmailWithNumericTLD() {
        assertFalse(validator.isValid("user@example.123", context));
    }
}