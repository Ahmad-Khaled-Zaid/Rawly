package com.rawly.webapp.dto;

import com.rawly.webapp.domain.Gender;
import com.rawly.webapp.validation.annotations.ValidEmail;
import com.rawly.webapp.validation.annotations.ValidFirstName;
import com.rawly.webapp.validation.annotations.ValidLastName;
import com.rawly.webapp.validation.annotations.ValidPhoneNumber;
import com.rawly.webapp.validation.annotations.ValidUsername;
import com.rawly.webapp.validation.validationGroups.UpdateGroup;

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
    @ValidFirstName(allowNullIfUpdating = true, groups = UpdateGroup.class)
    @Nullable
    private String firstName;

    @ValidLastName(allowNullIfUpdating = true, groups = UpdateGroup.class)
    @Nullable
    private String lastName;

    @ValidUsername(allowNullIfUpdating = true, groups = UpdateGroup.class)
    @Nullable
    private String username;

    @ValidEmail(allowNullIfUpdating = true, groups = UpdateGroup.class)
    @Nullable
    private String email;

    @ValidPhoneNumber(allowNullIfUpdating = true, groups = UpdateGroup.class)
    @Nullable
    private String phoneNumber;

    @Nullable
    private Gender gender;
}
