package com.rawly.webapp.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rawly.webapp.model.Gender;
import com.rawly.webapp.validation.annotations.ValidEmail;
import com.rawly.webapp.validation.annotations.ValidFirstName;
import com.rawly.webapp.validation.annotations.ValidLastName;
import com.rawly.webapp.validation.annotations.ValidPassword;
import com.rawly.webapp.validation.annotations.ValidPhoneNumber;
import com.rawly.webapp.validation.annotations.ValidUsername;
import com.rawly.webapp.validation.validationGroups.ICreateGroup;

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
    @ValidFirstName(groups = ICreateGroup.class)
    private String firstName;

    @ValidLastName(groups = ICreateGroup.class)
    private String lastName;

    @ValidUsername(groups = ICreateGroup.class)
    private String username;

    @ValidEmail(groups = ICreateGroup.class)
    private String email;

    @NotNull(message = "Gender is required.", groups = ICreateGroup.class)
    private Gender gender;

    @ValidPassword(groups = ICreateGroup.class)
    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Setter
    private String password;

    @ValidPhoneNumber(groups = ICreateGroup.class)
    private String phoneNumber;

    private List<String> roles;

}
