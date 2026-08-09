package com.emsee.employeemanagement.integration;

import com.emsee.employeemanagement.dto.LoginRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {

    }

    @Test
    void login_ShouldReturn200() throws Exception {

        LoginRequest request = LoginRequest.builder()
                .username("admin")
                .password("admin123")
                .build();

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.message")
                        .value("Login Successful"));
    }


    @Test
    void login_ShouldReturn401_WhenPasswordIsWrong() throws Exception {

        LoginRequest request = LoginRequest.builder()
                .username("admin")
                .password("wrongpassword")
                .build();

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    //accessToken field
    private String getAccessToken() throws Exception {

        LoginRequest request = LoginRequest.builder()
                .username("admin")
                .password("admin123")
                .build();

        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();

        JsonNode node = objectMapper.readTree(json);

        return node.get("accessToken").asText();
    }

    //Protected API Test
    @Test
    void getEmployees_ShouldReturn200_WhenTokenIsValid() throws Exception {

        String token = getAccessToken();

        mockMvc.perform(get("/api/v1/employees")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    //Unauthorized Test
    @Test
    void getEmployees_ShouldReturn401_WhenTokenMissing() throws Exception {

        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isUnauthorized());
    }

    //Invalid Token Test
    @Test
    void getEmployees_ShouldReturn401_WhenTokenInvalid() throws Exception {

        mockMvc.perform(get("/api/v1/employees")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());
    }
}


















































































