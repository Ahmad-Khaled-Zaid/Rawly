package com.rawly.webapp.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rawly.webapp.validation.annotations.ValidPassword;

import jakarta.validation.ConstraintValidatorContext;

class PasswordValidatorTest {

    private PasswordValidator validator;
    private ConstraintValidatorContext context;
    private ValidPassword validPasswordAnnotation;

    @BeforeEach
    void setUp() {
        validator = new PasswordValidator();
        context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(
                ConstraintValidatorContext.ConstraintViolationBuilder.class);

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addConstraintViolation()).thenReturn(context);

        validPasswordAnnotation = mock(ValidPassword.class);
        when(validPasswordAnnotation.min()).thenReturn(8);
        when(validPasswordAnnotation.max()).thenReturn(50);

        validator.initialize(validPasswordAnnotation);
    }

    // Unique and meaningful test cases
    @Test
    void testPasswordWithNullChar() {
        assertFalse(validator.isValid("Abcd1!\0ef", context));
    }

    @Test
    void testPasswordWithEmoji() {
        assertFalse(validator.isValid("Abcd1!😀ef", context));
    }

    @Test
    void testPasswordWithNonLatin() {
        assertFalse(validator.isValid("Пароль1!", context));
    }

    @Test
    void testPasswordWithChinese() {
        assertFalse(validator.isValid("密码Abc1!", context));
    }

    @Test
    void testPasswordWithAccented() {
        assertFalse(validator.isValid("Ábcdef1!", context));
    }

    @Test
    void testPasswordWithAllDigitsAndSpecial() {
        assertFalse(validator.isValid("1234567!", context));
    }

    @Test
    void testPasswordWithAllLettersAndSpecial() {
        assertFalse(validator.isValid("abcDefg!", context));
    }

    @Test
    void testPasswordWithAllUppercaseAndSpecial() {
        assertFalse(validator.isValid("ABCDEFG!", context));
    }

    @Test
    void testPasswordWithAllLowercaseAndSpecial() {
        assertFalse(validator.isValid("abcdefg!", context));
    }

    @Test
    void testPasswordWithAllSpecialAndUppercase() {
        assertFalse(validator.isValid("!@#ABCD!", context));
    }

    @Test
    void testPasswordWithAllSpecialAndLowercase() {
        assertFalse(validator.isValid("!@#abcd!", context));
    }

    @Test
    void testPasswordWithAllSpecialAndDigits() {
        assertFalse(validator.isValid("!@#1234!", context));
    }

    @Test
    void testPasswordWithAllSpecialAndLetters() {
        assertFalse(validator.isValid("!@#abcdA!", context));
    }

    @Test
    void testPasswordWithNoSpecialNoDigit() {
        assertFalse(validator.isValid("Abcdefgh", context));
    }

    @Test
    void testPasswordWithNoSpecialNoUpper() {
        assertFalse(validator.isValid("abcdefg1", context));
    }

    @Test
    void testPasswordWithNoSpecialNoLower() {
        assertFalse(validator.isValid("ABCDEFG1", context));
    }

    @Test
    void testPasswordWithNoSpecialNoLetter() {
        assertFalse(validator.isValid("12345678", context));
    }

    @Test
    void testPasswordWithNoSpecialNoLetterNoDigit() {
        assertFalse(validator.isValid("!!!!!!!!", context));
    }

    @Test
    void testPasswordWithWhitespaceInMiddle() {
        assertFalse(validator.isValid("Abcd ef1!", context));
    }

    @Test
    void testPasswordWithTabInMiddle() {
        assertFalse(validator.isValid("Abcd\t1!ef", context));
    }

    @Test
    void testPasswordWithNewlineInMiddle() {
        assertFalse(validator.isValid("Abcd\n1!ef", context));
    }

    @Test
    void testPasswordWithCarriageReturn() {
        assertFalse(validator.isValid("Abcd\r1!ef", context));
    }

    @Test
    void testPasswordWithFormFeed() {
        assertFalse(validator.isValid("Abcd\f1!ef", context));
    }

    @Test
    void testPasswordWithBackspace() {
        assertFalse(validator.isValid("Abcd\b1!ef", context));
    }

    @Test
    void testPasswordWithMultipleSpacesInside() {
        assertFalse(validator.isValid("Abc  def1!", context));
    }

    @Test
    void testPasswordWithMultipleTabsInside() {
        assertFalse(validator.isValid("Abc\t\tdef1!", context));
    }

    @Test
    void testPasswordWithMultipleNewlinesInside() {
        assertFalse(validator.isValid("Abc\n\ndef1!", context));
    }

    @Test
    void testPasswordWithMultipleCarriageReturnsInside() {
        assertFalse(validator.isValid("Abc\r\rdef1!", context));
    }

    @Test
    void testPasswordWithMultipleFormFeedsInside() {
        assertFalse(validator.isValid("Abc\f\fdef1!", context));
    }

    @Test
    void testPasswordWithMultipleBackspacesInside() {
        assertFalse(validator.isValid("Abc\b\bdef1!", context));
    }

    @Test
    void testPasswordWithAllValidTypes() {
        assertTrue(validator.isValid("A1!bcdef", context));
    }
    @Test
    void testPasswordWithAllValidTypesLong() {
        assertTrue(validator.isValid("A1!bcdefghijklmnopqrstuvwxyz1234567890!@#", context)); // check
    }

    @Test
    void testPasswordWithAllValidTypesShort() {
        assertTrue(validator.isValid("A1!bcdef", context));
    }

    // Add more unique cases up to 200
    @Test
    void testPasswordMinLength() {
        assertTrue(validator.isValid("A1!bcdef", context));
    }

    @Test
    void testPasswordMaxLength() {
        assertTrue(validator.isValid("A1!bcdefghijklmnopqrstuvwxyz1234567890!@#", context));
    }

    @Test
    void testPasswordTooShort() {
        assertFalse(validator.isValid("A1!bcde", context));
    }

    @Test
    void testPasswordTooLong() {
        assertFalse(
                validator.isValid("A1!bcdefghijklmnopqrstuvwxyz1234567890!@#A1!bcdefghijklmnopqrstuvwxyz", context));
    }

    @Test
    void testPasswordNull() {
        assertFalse(validator.isValid(null, context));
    }

    @Test
    void testPasswordEmpty() {
        assertFalse(validator.isValid("", context));
    }

    @Test
    void testPasswordOnlySpaces() {
        assertFalse(validator.isValid("        ", context));
    }

    @Test
    void testPasswordOnlyTabs() {
        assertFalse(validator.isValid("\t\t\t\t\t\t\t\t", context));
    }

    @Test
    void testPasswordOnlyNewlines() {
        assertFalse(validator.isValid("\n\n\n\n\n\n\n\n", context));
    }

    @Test
    void testPasswordOnlyCarriageReturns() {
        assertFalse(validator.isValid("\r\r\r\r\r\r\r\r", context));
    }

    @Test
    void testPasswordOnlyFormFeeds() {
        assertFalse(validator.isValid("\f\f\f\f\f\f\f\f", context));
    }

    @Test
    void testPasswordOnlyBackspaces() {
        assertFalse(validator.isValid("\b\b\b\b\b\b\b\b", context));
    }

    @Test
    void testPasswordWithLeadingSpace() {
        assertFalse(validator.isValid(" Abcd1!ef", context));
    }

    @Test
    void testPasswordWithTrailingSpace() {
        assertFalse(validator.isValid("Abcd1!ef ", context));
    }

    @Test
    void testPasswordWithLeadingTab() {
        assertFalse(validator.isValid("\tAbcd1!ef", context));
    }

    @Test
    void testPasswordWithTrailingTab() {
        assertFalse(validator.isValid("Abcd1!ef\t", context));
    }

    @Test
    void testPasswordWithLeadingNewline() {
        assertFalse(validator.isValid("\nAbcd1!ef", context));
    }

    @Test
    void testPasswordWithTrailingNewline() {
        assertFalse(validator.isValid("Abcd1!ef\n", context));
    }

    @Test
    void testPasswordWithLeadingCarriageReturn() {
        assertFalse(validator.isValid("\rAbcd1!ef", context));
    }

    @Test
    void testPasswordWithTrailingCarriageReturn() {
        assertFalse(validator.isValid("Abcd1!ef\r", context));
    }

    @Test
    void testPasswordWithLeadingFormFeed() {
        assertFalse(validator.isValid("\fAbcd1!ef", context));
    }

    @Test
    void testPasswordWithTrailingFormFeed() {
        assertFalse(validator.isValid("Abcd1!ef\f", context));
    }

    @Test
    void testPasswordWithLeadingBackspace() {
        assertFalse(validator.isValid("\bAbcd1!ef", context));
    }

    @Test
    void testPasswordWithTrailingBackspace() {
        assertFalse(validator.isValid("Abcd1!ef\b", context));
    }

    @Test
    void testPasswordWithMultipleTypesValid() {
        assertTrue(validator.isValid("A1!bC2@dE", context));
    }

    @Test
    void testPasswordWithMultipleTypesInvalid() {
        assertFalse(validator.isValid("A1bC2dE3", context));
    }

    @Test
    void testPasswordWithSpecialAtStart() {
        assertTrue(validator.isValid("!A1bcdef", context));
    }

    @Test
    void testPasswordWithSpecialAtEnd() {
        assertTrue(validator.isValid("A1bcdef!", context));
    }

    @Test
    void testPasswordWithDigitAtStart() {
        assertTrue(validator.isValid("1Abcdef!", context));
    }

    @Test
    void testPasswordWithDigitAtEnd() {
        assertTrue(validator.isValid("Abcdef!1", context));
    }

    @Test
    void testPasswordWithUpperAtStart() {
        assertTrue(validator.isValid("A1!bcdef", context));
    }

    @Test
    void testPasswordWithUpperAtEnd() {
        assertTrue(validator.isValid("1!bcdefA", context));
    }

    @Test
    void testPasswordWithLowerAtStart() {
        assertTrue(validator.isValid("a1!BCDEF", context));
    }

    @Test
    void testPasswordWithLowerAtEnd() {
        assertTrue(validator.isValid("1!BCDEFa", context));
    }

    @Test
    void testPasswordWithAllSameChar() {
        assertFalse(validator.isValid("aaaaaaaa", context));
    }

    @Test
    void testPasswordWithAllSameDigit() {
        assertFalse(validator.isValid("11111111", context));
    }

    @Test
    void testPasswordWithAllSameSpecial() {
        assertFalse(validator.isValid("!!!!!!!!", context));
    }

    @Test
    void testPasswordWithAllSameUpper() {
        assertFalse(validator.isValid("AAAAAAAA", context));
    }

    @Test
    void testPasswordWithAllSameLower() {
        assertFalse(validator.isValid("aaaaaaaa", context));
    }

    @Test
    void testPasswordWithAllSameType() {
        assertFalse(validator.isValid("bbbbbbbb", context));
    }

    @Test
    void testPasswordWithMixedCase() {
        assertTrue(validator.isValid("AbCdEf1!", context));
    }

    @Test
    void testPasswordWithMixedDigits() {
        assertTrue(validator.isValid("A1B2c3d!", context));
    }

    @Test
    void testPasswordWithMixedSpecials() {
        assertTrue(validator.isValid("A1!@#bcd", context));
    }

    @Test
    void testPasswordWithMixedTypes() {
        assertTrue(validator.isValid("A1a!bC2@", context));
    }

    @Test
    void testPasswordWithNoUpper() {
        assertFalse(validator.isValid("abcdef1!", context));
    }

    @Test
    void testPasswordWithNoLower() {
        assertFalse(validator.isValid("ABCDEF1!", context));
    }

    @Test
    void testPasswordWithNoDigit() {
        assertFalse(validator.isValid("Abcdefg!", context));
    }

    @Test
    void testPasswordWithNoSpecial() {
        assertFalse(validator.isValid("Abcdefg1", context));
    }

    @Test
    void testPasswordWithOnlyUpperAndDigit() {
        assertFalse(validator.isValid("ABCDEF12", context));
    }

    @Test
    void testPasswordWithOnlyLowerAndDigit() {
        assertFalse(validator.isValid("abcdef12", context));
    }

    @Test
    void testPasswordWithOnlyUpperAndSpecial() {
        assertFalse(validator.isValid("ABCDEF!@", context));
    }

    @Test
    void testPasswordWithOnlyLowerAndSpecial() {
        assertFalse(validator.isValid("abcdef!@", context));
    }

    @Test
    void testPasswordWithOnlyDigitAndSpecial() {
        assertFalse(validator.isValid("123456!@", context));
    }

    @Test
    void testPasswordWithUpperLowerDigit() {
        assertFalse(validator.isValid("Abcdef12", context));
    }

    @Test
    void testPasswordWithUpperLowerSpecial() {
        assertFalse(validator.isValid("Abcdef!@", context));
    }

    @Test
    void testPasswordWithLowerDigitSpecial() {
        assertFalse(validator.isValid("abcdef1!", context));
    }

    @Test
    void testPasswordWithUpperDigitSpecial() {
        assertFalse(validator.isValid("ABCDEF1!", context));
    }
}
