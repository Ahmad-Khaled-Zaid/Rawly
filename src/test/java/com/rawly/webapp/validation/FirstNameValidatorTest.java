package com.rawly.webapp.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rawly.webapp.validation.annotations.ValidFirstName;

import jakarta.validation.ConstraintValidatorContext;

public class FirstNameValidatorTest {
    private FirstNameValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new FirstNameValidator();
        context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(
                ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ValidFirstName annotation = mock(ValidFirstName.class);

        when(annotation.min()).thenReturn(3);
        when(annotation.max()).thenReturn(50);

        validator.initialize(annotation);
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addConstraintViolation()).thenReturn(context);

    }

    @Test
    void nullFirstNameShouldFail() {
        assertFalse(validator.isValid(null, context));
    }

    @Test
    void emptyFirstNameShouldFail() {
        assertFalse(validator.isValid("", context));
        assertFalse(validator.isValid("   ", context));
    }

    @Test
    void leadingOrTrailingWhitespaceShouldFail() {
        assertFalse(validator.isValid(" John", context));
        assertFalse(validator.isValid("John ", context));
        assertFalse(validator.isValid(" John ", context));
        assertFalse(validator.isValid("Mary Jane ", context));
        assertFalse(validator.isValid(" Mary Jane", context));
    }

    @Test
    void lengthLessThanThreeShouldFail() {
        assertFalse(validator.isValid("Jo", context));
        assertFalse(validator.isValid("A", context));
        assertFalse(validator.isValid("AB", context));
    }

    @Test
    void lengthGreaterThanFiftyShouldFail() {
        String longName = "a".repeat(51);
        assertFalse(validator.isValid(longName, context));
        String longCompound = "John ".repeat(11); // 55 chars
        assertFalse(validator.isValid(longCompound.trim(), context));
    }

    @Test
    void validSingleWordNamesShouldPass() {
        assertTrue(validator.isValid("John", context));
        assertTrue(validator.isValid("Alice", context));
        assertTrue(validator.isValid("Zoe", context));
        assertTrue(validator.isValid("ALEX", context));
        assertTrue(validator.isValid("alex", context));
    }

    @Test
    void validCompoundNamesShouldPass() {
        assertTrue(validator.isValid("Mary Jane", context));
        assertTrue(validator.isValid("John Paul", context));
        assertTrue(validator.isValid("Anna Marie", context));
    }

    @Test
    void multipleSpacesBetweenWordsShouldFail() {
        assertFalse(validator.isValid("Mary  Jane", context));
        assertFalse(validator.isValid("John   Paul", context));
    }

    @Test
    void invalidCharactersShouldFail() {
        assertFalse(validator.isValid("Jo3n", context));
        assertFalse(validator.isValid("Mary-Jane", context));
        assertFalse(validator.isValid("Ali_Omar", context));
        assertFalse(validator.isValid("John#", context));
        assertFalse(validator.isValid("O'Connor", context));
        assertFalse(validator.isValid("Anna*", context));
        assertFalse(validator.isValid("Jane!", context));
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
    void namesWithNonBreakingSpaceShouldFail() {
        assertFalse(validator.isValid("John\u00A0Doe", context));
    }

    @Test
    void minAndMaxLengthNamesShouldPass() {
        String minLen = "abc";
        String maxLen = "a".repeat(50);
        assertTrue(validator.isValid(minLen, context));
        assertTrue(validator.isValid(maxLen, context));
    }

    @Test
    void namesWithTabOrNewlineShouldFail() {
        assertFalse(validator.isValid("John\tDoe", context));
        assertFalse(validator.isValid("John\nDoe", context));
    }

    @Test
    void validNameWithMixedCaseShouldPass() {
        assertTrue(validator.isValid("John", context));
        assertTrue(validator.isValid("jOhN", context));
        assertTrue(validator.isValid("Mary Jane", context));
    }

}
