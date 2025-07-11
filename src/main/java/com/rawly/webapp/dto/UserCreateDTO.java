package com.rawly.webapp.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rawly.webapp.domain.Gender;
import com.rawly.webapp.validation.annotations.ValidEmail;
import com.rawly.webapp.validation.annotations.ValidFirstName;
import com.rawly.webapp.validation.annotations.ValidLastName;
import com.rawly.webapp.validation.annotations.ValidPassword;
import com.rawly.webapp.validation.annotations.ValidPhoneNumber;
import com.rawly.webapp.validation.annotations.ValidUsername;
import com.rawly.webapp.validation.validationGroups.CreateGroup;

import jakarta.validation.constraints.NotNull;
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

    @ValidUsername(groups = CreateGroup.class)
    private String username;

    @ValidEmail(groups = CreateGroup.class)
    private String email;

    @NotNull(message = "Gender is required.", groups = CreateGroup.class)
    private Gender gender;

    @ValidPassword(groups = CreateGroup.class)
    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Setter
    private String password;

    @ValidPhoneNumber(groups = CreateGroup.class)
    private String phoneNumber;

    private List<String> roles;

}
