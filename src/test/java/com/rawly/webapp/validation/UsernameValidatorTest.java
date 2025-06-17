package com.rawly.webapp.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rawly.webapp.validation.annotations.ValidUsername;

import jakarta.validation.ConstraintValidatorContext;

import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UsernameValidatorTest {

    private UsernameValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new UsernameValidator();
        context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(
                ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ValidUsername annotation = mock(ValidUsername.class);

        when(annotation.min()).thenReturn(5);
        when(annotation.max()).thenReturn(50);

        validator.initialize(annotation);

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addConstraintViolation()).thenReturn(context);
    }

    @Test
    void testNullUsername() {
        assertFalse(validator.isValid(null, context));
    }

    @Test
    void testEmptyUsername() {
        assertFalse(validator.isValid("", context));
    }

    @Test
    void testWhitespaceUsername() {
        assertFalse(validator.isValid("   ", context));
    }

    @Test
    void testUsernameTooShort() {
        assertFalse(validator.isValid("abcd", context)); // 4 chars
    }

    @Test
    void testUsernameTooLong() {
        String longUsername = "a".repeat(51); // 51 chars
        assertFalse(validator.isValid(longUsername, context));
    }

    @Test
    void testUsernameAtMinLength() {
        assertTrue(validator.isValid("abcde", context)); // 5 chars
    }

    @Test
    void testUsernameAtMaxLength() {
        String maxUsername = "a".repeat(50); // 50 chars
        assertTrue(validator.isValid(maxUsername, context));
    }

    @Test
    void testUsernameStartsWithNonLetter() {
        assertFalse(validator.isValid("1abcde", context));
        assertFalse(validator.isValid("_abcde", context));
    }

    @Test
    void testUsernameWithInvalidCharacters() {
        assertFalse(validator.isValid("abc$de", context));
        assertFalse(validator.isValid("abc de", context));
        assertFalse(validator.isValid("abc-de", context));
    }

    @Test
    void testUsernameWithLeadingUnderscore() {
        assertFalse(validator.isValid("_abcde", context));
    }

    @Test
    void testUsernameWithTrailingUnderscore() {
        assertFalse(validator.isValid("abcde_", context));
    }

    @Test
    void testUsernameWithConsecutiveUnderscores() {
        assertFalse(validator.isValid("ab__cde", context));
    }

    @Test
    void testUsernameWithSingleUnderscore() {
        assertTrue(validator.isValid("abc_de", context));
    }

    @Test
    void testUsernameWithDigits() {
        assertTrue(validator.isValid("abc123", context));
    }

    @Test
    void testValidUsernames() {
        assertTrue(validator.isValid("Alice", context));
        assertTrue(validator.isValid("alice_bob", context));
        assertTrue(validator.isValid("Alice123", context));
        assertTrue(validator.isValid("A_b_c_d_e", context));
    }

    @Test
    void nonEnglishLettersShouldFail() {
        assertFalse(validator.isValid("أحمد", context));
        assertFalse(validator.isValid("محمد", context));
        assertFalse(validator.isValid("Алексей", context));
        assertFalse(validator.isValid("张伟", context));
        assertFalse(validator.isValid("Jöhn", context));
        assertFalse(validator.isValid("Renée", context));
    }

    @Test
    void testUsernameOnlyDigitsShouldFail() {
        assertFalse(validator.isValid("123456", context));
    }

}
