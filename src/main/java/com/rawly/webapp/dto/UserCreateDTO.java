package com.rawly.webapp.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rawly.webapp.model.Gender;
import com.rawly.webapp.validation.annotations.ValidEmail;
import com.rawly.webapp.validation.annotations.ValidFirstName;
import com.rawly.webapp.validation.annotations.ValidLastName;
import com.rawly.webapp.validation.annotations.ValidPhoneNumber;
import com.rawly.webapp.validation.annotations.ValidUsername;
import com.rawly.webapp.validation.validationGroups.CreateGroup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserCreateDTO {
    @ValidFirstName(groups = CreateGroup.class)
    private String firstName;

    @ValidLastName(groups = CreateGroup.class)
    private String lastName;

    @NotBlank(message = "Username is required.", groups = CreateGroup.class)
    @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters.", groups = CreateGroup.class)
    @ValidUsername(message = "Username must be 5-50 characters long, start with a letter, contain only letters, digits, or underscores, cannot start or end with an underscore, and must not contain consecutive underscores or the '@' symbol.", groups = CreateGroup.class)
    private String username;

    @Email(message = "Please provide a valid email address.", groups = CreateGroup.class)
    @NotBlank(message = "Email is required.", groups = CreateGroup.class)
    @ValidEmail(message = "Please enter a valid email address", groups = CreateGroup.class)
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Email must be in a valid format.", groups = CreateGroup.class)
    private String email;

    @NotNull(message = "Gender is required.", groups = CreateGroup.class)
    private Gender gender;

    @NotBlank(message = "Password is required.", groups = CreateGroup.class)
    @Size(min = 8, max = 100, message = "Password must be at least 8 characters.", groups = CreateGroup.class)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+{}\\[\\]:;<>,.?~\\-=/\\\\|'\"])[A-Za-z\\d!@#$%^&*()_+{}\\[\\]:;<>,.?~\\-=/\\\\|'\"]{8,}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one number and one special character.", groups = CreateGroup.class)
    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Setter
    private String password;

    @NotBlank(message = "Phone number is required.", groups = CreateGroup.class)
    @ValidPhoneNumber(message = "Phone number must be between 7 and 12 digits", groups = CreateGroup.class)
    private String phoneNumber;

    private List<String> roles;

}
