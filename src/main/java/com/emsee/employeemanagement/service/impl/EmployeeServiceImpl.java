package com.emsee.employeemanagement.service.impl;

import com.emsee.employeemanagement.dto.EmployeeRequest;
import com.emsee.employeemanagement.dto.EmployeeResponse;
import com.emsee.employeemanagement.entity.Employee;
import com.emsee.employeemanagement.enums.EmployeeStatus;
import com.emsee.employeemanagement.mapper.EmployeeMapper;
import com.emsee.employeemanagement.repository.EmployeeRepository;
import com.emsee.employeemanagement.service.EmployeeService;
import com.emsee.employeemanagement.specification.EmployeeSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.emsee.employeemanagement.exceptions.ResourceNotFoundException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;


    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        log.info("Creating employee with email: {}", request.getEmail());
        Employee employee = EmployeeMapper.toEntity(request);
        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created successfully with ID: {}", savedEmployee.getEmployeeId());
        return EmployeeMapper.toResponse(savedEmployee);
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        log.info("Fetching employee with ID: {}", id);
        Employee employee = employeeRepository.
                findById(id).
                orElseThrow(() ->
                        new com.emsee.employeemanagement.exceptions.ResourceNotFoundException("Employee not found with ID: " + id));
        log.info("Employee found with ID: {}", id);
        return EmployeeMapper.toResponse(employee);

    }


    @Override
    public Page<EmployeeResponse> getAllEmployees(Pageable pageable) {

        log.info("Fetching employees with pagination. Page: {}, Size: {}",
                pageable.getPageNumber(),
                pageable.getPageSize());

        return employeeRepository
                .findAll(pageable)
                .map(EmployeeMapper::toResponse);
    }


    @Override
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        log.info("Updating employee with ID: {}", id);
        Employee employee = employeeRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Employee not found with ID: " + id));

        EmployeeMapper.updateEntity(employee, request);

        Employee updateEmployee = employeeRepository.save(employee);
        log.info("Employee updated successfully.");
        return EmployeeMapper.toResponse(updateEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        log.info("Deleting employee with ID: {}", id);
        Employee employee = employeeRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Employee not found with ID: " + id));
        log.info("Employee deleted successfully.");
        employeeRepository.delete(employee);
        //employeeRepository.deleteById(id);//if there was no id found, later when we add a custom or @controllerAdvice, this will become a clean 404 Not found exception
    }

//    @Override
//    public Page<EmployeeResponse> getEmployeesByDepartment(String department, Pageable pageable) {
//        return employeeRepository.findByDepartment(department,pageable).map(EmployeeMapper::toResponse);
//    }
//
//    @Override
//    public Page<EmployeeResponse> getEmployeesByDepartmentAndStatus(
//            String department,
//            EmployeeStatus status,
//            Pageable pageable) {
//
//        return employeeRepository
//                .findByDepartmentAndStatus(department, status, pageable)
//                .map(EmployeeMapper::toResponse);
//    }

    @Override
    public Page<EmployeeResponse> searchEmployees(
            String department,
            EmployeeStatus status,
            Pageable pageable) {
        //where(org.springframework.data.jpa.domain.Specification<T>)' is deprecated since version 3.5.0 and marked for removal
        //Specification<Employee> specification = Specification.where(null);
        Specification<Employee> specification = Specification.unrestricted();



        if (department != null && !department.isBlank()) {
            specification = specification.and(
                    EmployeeSpecification.hasDepartment(department)
            );
        }

        if (status != null) {
            specification = specification.and(
                    EmployeeSpecification.hasStatus(status)
            );
        }

        return employeeRepository
                .findAll(specification, pageable)
                .map(EmployeeMapper::toResponse);
    }


}
