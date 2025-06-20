package com.rawly.webapp.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rawly.webapp.dto.UserCreateDTO;
import com.rawly.webapp.dto.UserUpdateDTO;
import com.rawly.webapp.validation.annotations.ValidEmail;
import com.rawly.webapp.validation.annotations.ValidFirstName;
import com.rawly.webapp.validation.annotations.ValidLastName;
import com.rawly.webapp.validation.annotations.ValidPhoneNumber;
import com.rawly.webapp.validation.annotations.ValidUsername;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @EqualsAndHashCode.Include
    @Column(updatable = false)
    private Long id;

    @ValidFirstName
    @Column(nullable = false)
    private String firstName;

    @ValidLastName
    @Column(nullable = false)
    private String lastName;

    @ValidUsername
    @Column(unique = true, nullable = false)
    private String username;

    @ValidEmail
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull(message = "Gender is required.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    @ValidPhoneNumber
    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Builder.Default
    @JsonIgnore
    private boolean accountNonExpired = true;

    @Builder.Default
    @JsonIgnore
    private boolean accountNonLocked = true;

    @Builder.Default
    @JsonIgnore
    private boolean credentialsNonExpired = true;

    @Builder.Default
    @JsonIgnore
    private boolean isEnabled = true;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public void updateFromDTO(UserUpdateDTO userDetails) {
        if (userDetails.getFirstName() != null) {
            this.firstName = userDetails.getFirstName();
        }
        if (userDetails.getLastName() != null) {
            this.lastName = userDetails.getLastName();
        }
        if (userDetails.getEmail() != null) {
            this.email = userDetails.getEmail();
        }
        if (userDetails.getPhoneNumber() != null) {
            this.phoneNumber = userDetails.getPhoneNumber();
        }
        if (userDetails.getUsername() != null) {
            this.username = userDetails.getUsername();
        }
        if (userDetails.getGender() != null) {
            this.gender = userDetails.getGender();
        }
    }

    public static User createFromDTO(UserCreateDTO userDetails, String encodedPassword, Set<Role> assignedRolesSet) {
        User user = new User();
        user.firstName = userDetails.getFirstName();
        user.lastName = userDetails.getLastName();
        user.username = userDetails.getUsername();
        user.email = userDetails.getEmail();
        user.gender = userDetails.getGender();
        user.password = encodedPassword;
        user.phoneNumber = userDetails.getPhoneNumber();
        user.roles = assignedRolesSet;
        return user;
    }

}
