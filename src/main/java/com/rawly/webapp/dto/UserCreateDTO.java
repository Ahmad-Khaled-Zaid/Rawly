package com.rawly.webapp.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rawly.webapp.validation.CreateGroup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "First name is required.", groups = CreateGroup.class)
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.", groups = CreateGroup.class)
    @Pattern(regexp = "^[A-Za-z ]+$", message = "First name must contain only English letters and spaces.", groups = CreateGroup.class)
    private String firstName;

    @NotBlank(message = "Last name is required.", groups = CreateGroup.class)
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.", groups = CreateGroup.class)
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Last name must contain only English letters and spaces.", groups = CreateGroup.class)
    private String lastName;

    @NotBlank(message = "Username is required.", groups = CreateGroup.class)
    @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters.", groups = CreateGroup.class)
    @Pattern(regexp = "^(?!.*@)(?!.*__)(?!_)(?![0-9])[A-Za-z0-9_]{5,50}(?<!_)$", message = "Username must be 5-50 characters long, start with a letter, contain only letters, numbers, or underscores, and must not start or end with an underscore or contain double underscores.", groups = CreateGroup.class)
    private String username;

    @Email(message = "Please provide a valid email address.", groups = CreateGroup.class)
    @NotBlank(message = "Email is required.", groups = CreateGroup.class)
    private String email;

    @NotBlank(message = "Password is required.", groups = CreateGroup.class)
    @Size(min = 8, max = 100, message = "Password must be at least 8 characters.", groups = CreateGroup.class)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+{}\\[\\]:;<>,.?~\\-=/\\\\|'\"])[A-Za-z\\d!@#$%^&*()_+{}\\[\\]:;<>,.?~\\-=/\\\\|'\"]{8,}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one number and one special character.", groups = CreateGroup.class)
    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Setter
    private String password;

    @NotBlank(message = "Phone number is required.", groups = CreateGroup.class)
    @Pattern(regexp = "^[0-9]{7,12}$", message = "Phone number must be between 7 and 12 digits", groups = CreateGroup.class)
    private String phoneNumber;

    private List<String> roles;

}
