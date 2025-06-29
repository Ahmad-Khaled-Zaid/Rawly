package com.rawly.webapp.dto;

import com.rawly.webapp.model.Gender;
import com.rawly.webapp.validation.annotations.ValidEmail;
import com.rawly.webapp.validation.annotations.ValidFirstName;
import com.rawly.webapp.validation.annotations.ValidLastName;
import com.rawly.webapp.validation.annotations.ValidPhoneNumber;
import com.rawly.webapp.validation.annotations.ValidUsername;
import com.rawly.webapp.validation.validationGroups.IUpdateGroup;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserUpdateDTO {
    @ValidFirstName(isUpdate = true, groups = IUpdateGroup.class)
    @Nullable
    private String firstName;

    @ValidLastName(isUpdate = true, groups = IUpdateGroup.class)
    @Nullable
    private String lastName;

    @ValidUsername(isUpdate = true, groups = IUpdateGroup.class)
    @Nullable
    private String username;

    @ValidEmail(isUpdate = true, groups = IUpdateGroup.class)
    @Nullable
    private String email;

    @ValidPhoneNumber(isUpdate = true, groups = IUpdateGroup.class)
    @Nullable
    private String phoneNumber;

    @Nullable
    private Gender gender;
}
