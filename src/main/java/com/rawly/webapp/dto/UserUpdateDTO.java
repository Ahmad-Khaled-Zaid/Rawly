package com.rawly.webapp.dto;

import com.rawly.webapp.validation.UpdateGroup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserUpdateDTO {
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.", groups = UpdateGroup.class)
    @Pattern(regexp = "^[A-Za-z ]+$", message = "First name must contain only English letters.", groups = UpdateGroup.class)
    private String firstName;

    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.", groups = UpdateGroup.class)
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Last name must contain only English letters.", groups = UpdateGroup.class)
    private String lastName;

    @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters.", groups = UpdateGroup.class)
    @Pattern(regexp = "^(?!.*@)(?!.*__)(?!_)(?![0-9])[A-Za-z0-9_]{5,50}(?<!_)$", message = "Username must be 5-50 characters long, start with a letter, contain only letters, numbers, or underscores, and must not start or end with an underscore or contain double underscores.", groups = UpdateGroup.class)
    private String username;

    @Email(message = "Please Enter Valid Email", groups = UpdateGroup.class)
    private String email;

    @Pattern(regexp = "^[0-9]{7,12}$", message = "Phone number must be between 7 and 12 digits", groups = UpdateGroup.class)
    private String phoneNumber;
}
