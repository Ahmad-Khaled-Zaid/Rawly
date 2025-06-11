package com.rawly.webapp.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rawly.webapp.dto.UserCreateDTO;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateUser_Success() throws Exception {
        UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                .firstName("Ahmad")
                .lastName("Zaid")
                .email("ahmadk.zaid97@gmail.com")
                .username("ahmad_zaid")
                .password("Ahmad@12345")
                .phoneNumber("0770640184")
                .build();

        mockMvc.perform(post("/api/users/createUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("Ahmad"))
                .andExpect(jsonPath("$.lastName").value("Zaid"))
                .andExpect(jsonPath("$.phoneNumber").value("0770640184"))
                .andExpect(jsonPath("$.username").value("ahmad_zaid"))
                .andExpect(jsonPath("$.email").value("ahmadk.zaid97@gmail.com"))
                .andDo(print());
    }
}
