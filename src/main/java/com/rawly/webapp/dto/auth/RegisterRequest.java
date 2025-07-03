package com.rawly.webapp.dto.auth;

import com.rawly.webapp.domain.Gender;
import com.rawly.webapp.validation.annotations.PasswordMatches;
import com.rawly.webapp.validation.annotations.ValidEmail;
import com.rawly.webapp.validation.annotations.ValidFirstName;
import com.rawly.webapp.validation.annotations.ValidLastName;
import com.rawly.webapp.validation.annotations.ValidPassword;
import com.rawly.webapp.validation.annotations.ValidPhoneNumber;
import com.rawly.webapp.validation.annotations.ValidUsername;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@PasswordMatches
public class RegisterRequest {
    @ValidUsername
    private String username;
    @ValidEmail
    private String email;
    @ValidPhoneNumber
    private String phoneNumber;
    @ValidFirstName
    private String firstName;
    @ValidLastName
    private String lastName;
    @ValidPassword
    private String password;
    @ValidPassword
    private String confirmedPassword;
    @NotNull(message = "Gender is required")
    private Gender gender;
}
