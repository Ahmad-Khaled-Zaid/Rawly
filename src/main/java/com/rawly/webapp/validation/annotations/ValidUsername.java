package com.rawly.webapp.validation.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.rawly.webapp.validation.UsernameValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = UsernameValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsername {
    String message() default "Invalid username.";

    int min() default 5;

    int max() default 50;

    boolean isUpdate() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
