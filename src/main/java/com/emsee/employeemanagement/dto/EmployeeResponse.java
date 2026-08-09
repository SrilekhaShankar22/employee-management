package com.emsee.employeemanagement.dto;

import com.emsee.employeemanagement.enums.EmployeeStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Employee Request DTO")
public class EmployeeResponse {

    @Schema(example = "1")
    private Long employeeId;

    @Schema(example = "Srilekha")
    private String firstName;

    @Schema(example = "Shankar")
    private String lastName;

    @Schema(example = "ss22@gmail.com")
    private String email;


    private String phoneNumber;

    private String department;

    private String designation;

    private BigDecimal salary;

    private LocalDate joiningDate;

    private EmployeeStatus status;
}