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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    /**
     * TODO: prevent username to accept email value , will check in java code.
     * TODO: igonore id from Req and Res
     **/

    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank(message = "First name is required.")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "First name must contain only English letters.")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Last name must contain only English letters.")
    @Column(nullable = false)
    private String lastName;

    @NotBlank(message = "Username is required.")
    @Column(unique = true, nullable = false)
    @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters.")
    @Pattern(regexp = "^(?!.*@)(?!.*__)(?!_)(?![0-9])[A-Za-z0-9_]{5,50}(?<!_)$", message = "Username must be 5-50 characters long, start with a letter, contain only letters, numbers, or underscores, and must not start or end with an underscore or contain double underscores.")
    private String username;

    @Email(message = "Please provide a valid email address.")
    @NotBlank(message = "Email is required.")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 100, message = "Password must be at least 8 characters.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+{}\\[\\]:;<>,.?~\\-=/\\\\|'\"])[A-Za-z\\d!@#$%^&*()_+{}\\[\\]:;<>,.?~\\-=/\\\\|'\"]{8,}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one number and one special character.")
    @Column(nullable = false)
    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank(message = "Phone number is required.")
    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^[0-9]{7,12}$", message = "Phone number must be between 7 and 12 digits")
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
        this.firstName = userDetails.getFirstName();
        this.lastName = userDetails.getLastName();
        this.email = userDetails.getEmail();
        this.phoneNumber = userDetails.getPhoneNumber();
        this.username = userDetails.getUsername();
    }

    public static User createFromDTO(UserCreateDTO userDetails,String encodedPassword, Set<Role> assignedRolesSet) {
        User user = new User();
        user.firstName = userDetails.getFirstName();
        user.lastName = userDetails.getLastName();
        user.username = userDetails.getUsername();
        user.email = userDetails.getEmail();
        user.password = encodedPassword;
        user.phoneNumber = userDetails.getPhoneNumber();
        user.roles = assignedRolesSet;
        return user;
    }

}
