package com.rawly.webapp.dto;

import com.rawly.webapp.model.Gender;
import com.rawly.webapp.validation.annotations.ValidFirstName;
import com.rawly.webapp.validation.annotations.ValidLastName;
import com.rawly.webapp.validation.annotations.ValidUsername;
import com.rawly.webapp.validation.validationGroups.UpdateGroup;

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
    @ValidFirstName(message = "First name must be 3-50 English letters long, and may contain one space if compound.", groups = UpdateGroup.class)
    private String firstName;

    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.", groups = UpdateGroup.class)
    @ValidLastName(message = "Last name must be 3-50 English letters long, and may contain one space if compound.", groups = UpdateGroup.class)
    private String lastName;

    @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters.", groups = UpdateGroup.class)
    @ValidUsername(message = "Username must be 5-50 characters long, start with a letter, contain only letters, digits, or underscores, cannot start or end with an underscore, and must not contain consecutive underscores or the '@' symbol.", groups = UpdateGroup.class)
    private String username;

    private String email;

    @Pattern(regexp = "^[0-9]{7,12}$", message = "Phone number must be between 7 and 12 digits", groups = UpdateGroup.class)
    private String phoneNumber;

    private Gender gender;
}
