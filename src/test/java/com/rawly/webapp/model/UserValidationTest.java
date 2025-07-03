package com.rawly.webapp.model;

import jakarta.validation.Validator;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.rawly.webapp.domain.model.User;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;

public class UserValidationTest {
    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private User buildValidUser() {
        return User.builder()
                .firstName("Ahmad")
                .lastName("Zaid")
                .username("ahmad_zaid")
                .email("ahmad@example.com")
                .password("Aa12345@")
                .phoneNumber("0791234567")
                .build();

    }

    @Test
    public void validUser_ShouldHaveNoViolations() {
        User user = buildValidUser();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }
}
