package com.emsee.employeemanagement.service;

import com.emsee.employeemanagement.dto.EmployeeRequest;
import com.emsee.employeemanagement.dto.EmployeeResponse;
import com.emsee.employeemanagement.enums.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeRequest request);

    EmployeeResponse getEmployeeById(Long id);

    Page<EmployeeResponse> getAllEmployees(Pageable pageable);

    EmployeeResponse updateEmployee(Long id, EmployeeRequest request);

    void deleteEmployee(Long id);

//    Page<EmployeeResponse> getEmployeesByDepartment(
//            String department,
//            Pageable pageable
//    );
//
//    Page<EmployeeResponse> getEmployeesByDepartmentAndStatus(
//            String department,
//            EmployeeStatus status,
//            Pageable pageable
//    );

    Page<EmployeeResponse> searchEmployees(
            String department,
            EmployeeStatus status,
            Pageable pageable
    );

}