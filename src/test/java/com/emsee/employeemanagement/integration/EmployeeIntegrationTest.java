package com.emsee.employeemanagement.integration;

import com.emsee.employeemanagement.dto.EmployeeRequest;
import com.emsee.employeemanagement.dto.LoginRequest;
import com.emsee.employeemanagement.enums.EmployeeStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EmployeeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    void setup() throws Exception {
        token = getAccessToken();
    }

    private String getAccessToken() throws Exception {

        LoginRequest loginRequest = LoginRequest.builder()
                .username("admin")
                .password("admin123")
                .build();

        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode node =
                objectMapper.readTree(result.getResponse().getContentAsString());

        return node.get("accessToken").asText();
    }

    @Test
    void createEmployee_ShouldReturn201() throws Exception {

        EmployeeRequest request = EmployeeRequest.builder()
                .firstName("Srilekha")
                .lastName("Shankar")
                .email("srilekha@test.com")
                .phoneNumber("9876543210")
                .department("IT")
                .designation("Software Engineer")
                .salary(new BigDecimal("60000"))
                .joiningDate(LocalDate.now())
                .status(EmployeeStatus.ACTIVE)
                .build();

        mockMvc.perform(post("/api/v1/employees")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.firstName").value("Srilekha"))
                .andExpect(jsonPath("$.data.department").value("IT"));
    }

    //Helper method for employee creation
    private EmployeeRequest getEmployeeRequest() {
        return EmployeeRequest.builder()
                .firstName("Test")
                .lastName("User")
                .email(UUID.randomUUID() + "@test.com")
                .phoneNumber("9876543210")
                .department("IT")
                .designation("Developer")
                .salary(new BigDecimal("50000"))
                .joiningDate(LocalDate.now())
                .status(EmployeeStatus.ACTIVE)
                .build();
    }

    private Long createEmployee() throws Exception {
        EmployeeRequest request = getEmployeeRequest();

        MvcResult result =
                mockMvc.perform(post("/api/v1/employees")
                                .header("Authorization","Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated())
                        .andReturn();

        JsonNode node =
                objectMapper.readTree(result.getResponse().getContentAsString());

        return node.get("data")
                .get("employeeId")
                .asLong();
    }


    //GET Employee By ID
    @Test
    void getEmployeeById_ShouldReturnEmployee() throws Exception {

        Long id = createEmployee();

        mockMvc.perform(get("/api/v1/employees/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }


    //UPDATE Employee
    @Test
    void updateEmployee_ShouldUpdateEmployee() throws Exception {

        EmployeeRequest request = EmployeeRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johnupdate@test.com")
                .phoneNumber("9876543210")
                .department("IT")
                .designation("Developer")
                .salary(new BigDecimal("50000"))
                .joiningDate(LocalDate.now())
                .status(EmployeeStatus.ACTIVE)
                .build();

        MvcResult createResult = mockMvc.perform(post("/api/v1/employees")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        JsonNode response =
                objectMapper.readTree(createResult.getResponse().getContentAsString());

        Long id = response.get("data").get("employeeId").asLong();

        request.setDesignation("Senior Developer");

        mockMvc.perform(put("/api/v1/employees/" + id)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.designation")
                        .value("Senior Developer"));
    }


    //DELETE Employee
    @Test
    void deleteEmployee_ShouldDeleteEmployee() throws Exception {

        EmployeeRequest request = EmployeeRequest.builder()
                .firstName("Delete")
                .lastName("User")
                .email("delete@test.com")
                .phoneNumber("9999999999")
                .department("IT")
                .designation("Developer")
                .salary(new BigDecimal("40000"))
                .joiningDate(LocalDate.now())
                .status(EmployeeStatus.ACTIVE)
                .build();

        MvcResult createResult = mockMvc.perform(post("/api/v1/employees")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        JsonNode response =
                objectMapper.readTree(createResult.getResponse().getContentAsString());

        Long id = response.get("data").get("employeeId").asLong();

        mockMvc.perform(delete("/api/v1/employees/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        mockMvc.perform(get("/api/v1/employees/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }


    //Search Employee
    @Test
    void searchEmployees_ShouldReturnMatchingEmployees() throws Exception {

        EmployeeRequest request = EmployeeRequest.builder()
                .firstName("Alice")
                .lastName("Smith")
                .email("alice@test.com")
                .phoneNumber("9999999999")
                .department("IT")
                .designation("Developer")
                .salary(new BigDecimal("50000"))
                .joiningDate(LocalDate.now())
                .status(EmployeeStatus.ACTIVE)
                .build();

        mockMvc.perform(post("/api/v1/employees")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/employees/search")
                        .header("Authorization", "Bearer " + token)
                        .param("department", "IT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].department").value("IT"));
    }

    //Pagination Test
    @Test
    void getEmployees_ShouldSupportPagination() throws Exception {

        mockMvc.perform(get("/api/v1/employees")
                        .header("Authorization", "Bearer " + token)
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.data.pageable.pageSize").value(5));
    }


    //Field Sorting Test
    @Test
    void getEmployees_ShouldSupportSorting() throws Exception {

        mockMvc.perform(get("/api/v1/employees")
                        .header("Authorization", "Bearer " + token)
                        .param("sort", "firstName,asc"))
                .andExpect(status().isOk());
    }

    //Validation Test
    @Test
    void createEmployee_ShouldReturn400_WhenValidationFails() throws Exception {

        EmployeeRequest request = EmployeeRequest.builder()
                .firstName("")
                .lastName("")
                .email("wrong-email")
                .build();

        mockMvc.perform(post("/api/v1/employees")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    //404 Not Found
    @Test
    void getEmployee_ShouldReturn404_WhenEmployeeNotFound() throws Exception {

        mockMvc.perform(get("/api/v1/employees/999999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message")
                        .value("Employee not found with ID: 999999"));
    }

    //401 Unauthorized(No Token)
    @Test
    void getEmployees_ShouldReturn401_WhenTokenMissing() throws Exception {

        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }

    //Invalid JWT
    @Test
    void getEmployees_ShouldReturn401_WhenTokenInvalid() throws Exception {

        mockMvc.perform(get("/api/v1/employees")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }

}


