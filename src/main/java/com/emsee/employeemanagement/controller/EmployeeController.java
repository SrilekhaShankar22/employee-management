package com.emsee.employeemanagement.controller;

import com.emsee.employeemanagement.dto.ApiResponse;
import com.emsee.employeemanagement.dto.EmployeeRequest;
import com.emsee.employeemanagement.dto.EmployeeResponse;
import com.emsee.employeemanagement.enums.EmployeeStatus;
import com.emsee.employeemanagement.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;


import java.util.List;

@Tag(name = "Employee API", description = "Employee Management REST APIs")
@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @Operation(summary = "Create Employee")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EmployeeResponse>> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        EmployeeResponse response = employeeService.createEmployee(request);
        ApiResponse<EmployeeResponse> apiResponse =
                ApiResponse.<EmployeeResponse>builder()
                        .success(true)
                        .message("Employee created successfully")
                        .data(response)
                        .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<Page<EmployeeResponse>>> getAllEmployees (@PageableDefault(

                page = 0,
                size = 5,
                sort = "employeeId",
                direction = Sort.Direction.ASC
        )
        Pageable pageable){

        Page<EmployeeResponse> response = employeeService.getAllEmployees(pageable);

        ApiResponse<Page<EmployeeResponse>> apiResponse =
                ApiResponse.<Page<EmployeeResponse>>builder()
                        .success(true)
                        .message("Employees fetched successfully")
                        .data(response)
                        .build();

        return ResponseEntity.ok(apiResponse);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<EmployeeResponse>> getEmployeeById(@PathVariable Long id) {
        EmployeeResponse response = employeeService.getEmployeeById(id);

        ApiResponse<EmployeeResponse> apiResponse =
                ApiResponse.<EmployeeResponse>builder()
                        .success(true)
                        .message("Employee fetched successfully")
                        .data(response)
                        .build();

        return ResponseEntity.ok(apiResponse);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EmployeeResponse>> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeRequest request) {

        EmployeeResponse response = employeeService.updateEmployee(id, request);
        ApiResponse<EmployeeResponse> apiResponse =
                ApiResponse.<EmployeeResponse>builder()
                        .success(true)
                        .message("Employee created successfully")
                        .data(response)
                        .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> deleteEmployee(@PathVariable Long id){
        employeeService.deleteEmployee(id);
        ApiResponse<Object> response =
                ApiResponse.builder()
                        .success(true)
                        .message("Employee deleted successfully")
                        .data(null)
                        .build();

        return ResponseEntity.ok(response);
    }


//    @GetMapping("/filter")
//    @PreAuthorize("hasRole(ADMIN)")
//    public ResponseEntity<ApiResponse<Page<EmployeeResponse>>> getEmployeeByDepartment(@RequestParam String department,
//        @PageableDefault(
//                page = 0,
//                size = 5,
//                sort = "employeeId"
//        ) Pageable pageable){
//
//        Page<EmployeeResponse> response = employeeService.getEmployeesByDepartment(department, pageable);
//
//        ApiResponse<Page<EmployeeResponse>> apiResponse =
//                ApiResponse.<Page<EmployeeResponse>>builder()
//                        .success(true)
//                        .message("Employee fetched by department successfully")
//                        .data(response)
//                        .build();
//
//        return ResponseEntity.ok(apiResponse);
//    }


//    @GetMapping("/filter")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<ApiResponse<Page<EmployeeResponse>>> filterEmployees(
//
//            @RequestParam String department,
//
//            @RequestParam EmployeeStatus status,
//
//            @PageableDefault(
//                    page = 0,
//                    size = 5,
//                    sort = "employeeId"
//            )
//            Pageable pageable) {
//
//        Page<EmployeeResponse> response =
//                employeeService.getEmployeesByDepartmentAndStatus(
//                        department,
//                        status,
//                        pageable);
//
//        ApiResponse<Page<EmployeeResponse>> apiResponse =
//                ApiResponse.<Page<EmployeeResponse>>builder()
//                        .success(true)
//                        .message("Employees fetched successfully")
//                        .data(response)
//                        .build();
//
//        return ResponseEntity.ok(apiResponse);
//    }


    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<Page<EmployeeResponse>>> searchEmployees(

            @RequestParam(required = false) String department,

            @RequestParam(required = false) EmployeeStatus status,

            @ParameterObject
            @PageableDefault(
                    page = 0,
                    size = 5,
                    sort = "employeeId",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable) {

        Page<EmployeeResponse> response =
                employeeService.searchEmployees(
                        department,
                        status,
                        pageable
                );

        ApiResponse<Page<EmployeeResponse>> apiResponse =
                ApiResponse.<Page<EmployeeResponse>>builder()
                        .success(true)
                        .message("Employees fetched successfully")
                        .data(response)
                        .build();

        return ResponseEntity.ok(apiResponse);
    }

}