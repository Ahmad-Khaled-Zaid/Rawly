package com.rawly.webapp.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import com.rawly.webapp.repository.RoleRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            List<String> defaultRoles = List.of("ADMIN", "USER", "MODERATOR");

            for (String roleName : defaultRoles) {
                // boolean exists = roleRepository.existsByName(roleName);
                // if (!exists) {
                // roleRepository.save(Role.builder().name(roleName).build());
                System.out.println("Created role: " + roleName);
                // }
            }
        };
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
        processor.setValidator(validator());
        return processor;
    }
}
