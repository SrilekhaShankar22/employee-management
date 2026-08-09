//package com.emsee.employeemanagement.controller;
//
//import com.emsee.employeemanagement.config.SecurityConfig;
//import com.emsee.employeemanagement.dto.EmployeeResponse;
//import com.emsee.employeemanagement.enums.EmployeeStatus;
//import com.emsee.employeemanagement.security.JwtAccessDeniedHandler;
//import com.emsee.employeemanagement.security.JwtAuthenticationEntryPoint;
//import com.emsee.employeemanagement.security.JwtAuthenticationFilter;
//import com.emsee.employeemanagement.security.JwtService;
//import com.emsee.employeemanagement.service.EmployeeService;
//import com.emsee.employeemanagement.service.impl.CustomUserDetailsService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(EmployeeController.class)
//@Import(SecurityConfig.class)
//class EmployeeControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private EmployeeService employeeService;
//
//    @MockitoBean
//    private AuthenticationManager authenticationManager;
//
//    @MockitoBean
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockitoBean
//    private JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    @MockitoBean
//    private JwtService jwtService;
//
//    @MockitoBean
//    private CustomUserDetailsService customUserDetailsService;
//
//    @MockitoBean
//    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//
//    @MockitoBean
//    private JwtAccessDeniedHandler jwtAccessDeniedHandler;
//
//    @Test
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    void getAllEmployees_ShouldReturnSuccess() throws Exception {
//
//        EmployeeResponse employee = EmployeeResponse.builder()
//                .employeeId(1L)
//                .firstName("John")
//                .lastName("Doe")
//                .email("john@gmail.com")
//                .department("IT")
//                .designation("Backend Developer")
//                .salary(new BigDecimal("75000"))
//                .joiningDate(LocalDate.now())
//                .status(EmployeeStatus.ACTIVE)
//                .build();
//
//        Page<EmployeeResponse> page =
//                new PageImpl<>(
//                        List.of(employee),
//                        PageRequest.of(0,5),
//                        1
//                );
//
//        when(employeeService.getAllEmployees(org.mockito.ArgumentMatchers.any()))
//                .thenReturn(page);
//
//        mockMvc.perform(get("/api/v1/employees")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message")
//                        .value("Employees fetched successfully"))
//                .andExpect(jsonPath("$.data.content[0].firstName")
//                        .value("John"));
//    }
//
//    @Test
//    void getEmployees_ShouldReturn401_WhenNotAuthenticated() throws Exception {
//
//        mockMvc.perform(get("/api/v1/employees"))
//                .andExpect(status().isUnauthorized());
//
//    }
//
//}