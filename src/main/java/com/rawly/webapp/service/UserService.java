package com.rawly.webapp.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rawly.webapp.dto.UserCreateDTO;
import com.rawly.webapp.dto.UserUpdateDTO;
import com.rawly.webapp.exception.DuplicateFieldException;
import com.rawly.webapp.exception.InvalidEmailException;
import com.rawly.webapp.exception.ResourceNotFoundException;
import com.rawly.webapp.model.Role;
import com.rawly.webapp.model.User;
import com.rawly.webapp.repository.RoleRepository;
import com.rawly.webapp.repository.UserRepository;
import com.rawly.webapp.util.EmailUtils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public User createUser(UserCreateDTO userDetails) {
        validateUserFields(userDetails);
        Set<Role> assignedRolesSet = new HashSet<>();
        String encodedPassword = passwordEncoder.encode(userDetails.getPassword());
        userDetails.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin && userDetails.getRoles() != null && !userDetails.getRoles().isEmpty()) {
            assignedRolesSet = roleRepository.findByNameIn(userDetails.getRoles());
        } else {
            Role role = roleRepository.getRoleByName("ROLE_USER");
            assignedRolesSet.add(role);
        }
        User user = User.createFromDTO(userDetails, encodedPassword, assignedRolesSet);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with Id: " + id + " is not found"));
    }

    public void updateUser(Long id, UserUpdateDTO userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with Id: " + id + " is not found"));
        if (userDetails.getEmail() != null && !EmailUtils.isValidEmail(userDetails.getEmail().trim())) {
            throw new InvalidEmailException("Please enter a valid email address");
        }

        user.updateFromDTO(userDetails);
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with Id: " + id + " is not found"));
        userRepository.delete(user);
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    public void validateUserFields(UserCreateDTO userDetails) {
        List<String> conflicts = new ArrayList<>();
        if (userDetails.getEmail() != null) {

        }
        if (userRepository.existsByEmail(userDetails.getEmail())) {
            conflicts.add("Email");
        }
        if (userRepository.existsByUsername(userDetails.getUsername())) {
            conflicts.add("UserName");
        }
        if (userRepository.existsByPhoneNumber(userDetails.getPhoneNumber())) {
            conflicts.add("PhoneNumber");
        }
        if (!conflicts.isEmpty()) {
            throw new DuplicateFieldException(conflicts);
        }
    }
}