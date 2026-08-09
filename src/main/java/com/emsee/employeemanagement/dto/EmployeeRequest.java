package com.emsee.employeemanagement.dto;

import com.emsee.employeemanagement.enums.EmployeeStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;

    private String phoneNumber;

    @NotBlank
    private String department;

    @NotBlank
    private String designation;

    @NotNull
    private BigDecimal salary;

    @NotNull
    private LocalDate joiningDate;

    @NotNull
    private EmployeeStatus status;
}