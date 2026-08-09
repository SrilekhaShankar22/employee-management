package com.emsee.employeemanagement.service.impl;

import com.emsee.employeemanagement.dto.EmployeeRequest;
import com.emsee.employeemanagement.dto.EmployeeResponse;
import com.emsee.employeemanagement.entity.Employee;
import com.emsee.employeemanagement.enums.EmployeeStatus;
import com.emsee.employeemanagement.exceptions.ResourceNotFoundException;
import com.emsee.employeemanagement.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeRequest request;

    @BeforeEach
    void setUp() {

        request = EmployeeRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@gmail.com")
                .phoneNumber("9876543210")
                .department("IT")
                .designation("Backend Developer")
                .salary(new BigDecimal("75000"))
                .joiningDate(LocalDate.now())
                .status(EmployeeStatus.ACTIVE)
                .build();

        employee = Employee.builder()
                .employeeId(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@gmail.com")
                .phoneNumber("9876543210")
                .department("IT")
                .designation("Backend Developer")
                .salary(new BigDecimal("75000"))
                .joiningDate(LocalDate.now())
                .status(EmployeeStatus.ACTIVE)
                .build();
    }

    @Test
    void createEmployee_ShouldReturnEmployeeResponse() {

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(employee);

        EmployeeResponse response =
                employeeService.createEmployee(request);

        assertNotNull(response);
        assertEquals(employee.getEmployeeId(), response.getEmployeeId());
        assertEquals(employee.getEmail(), response.getEmail());

        verify(employeeRepository, times(1))
                .save(any(Employee.class));
    }

    @Test
    void getEmployeeById_ShouldReturnEmployeeResponse() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        EmployeeResponse response =
                employeeService.getEmployeeById(1L);

        assertNotNull(response);
        assertEquals("John", response.getFirstName());

        verify(employeeRepository).findById(1L);
    }

    @Test
    void getEmployeeById_ShouldThrowException() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.getEmployeeById(1L)
        );

        verify(employeeRepository).findById(1L);
    }

    @Test
    void getAllEmployees_ShouldReturnPage() {

        Pageable pageable = PageRequest.of(0,5);

        Page<Employee> employeePage =
                new PageImpl<>(List.of(employee));

        when(employeeRepository.findAll(pageable))
                .thenReturn(employeePage);

        Page<EmployeeResponse> response =
                employeeService.getAllEmployees(pageable);

        assertEquals(1, response.getTotalElements());

        verify(employeeRepository).findAll(pageable);
    }

    @Test
    void updateEmployee_ShouldUpdateEmployee() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(employee);

        EmployeeResponse response =
                employeeService.updateEmployee(1L, request);

        assertNotNull(response);
        assertEquals("John", response.getFirstName());

        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void deleteEmployee_ShouldDeleteEmployee() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        doNothing()
                .when(employeeRepository)
                .delete(employee);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository).delete(employee);
    }

    @Test
    void deleteEmployee_ShouldThrowException() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.deleteEmployee(1L)
        );

        verify(employeeRepository, never())
                .delete(any(Employee.class));
    }

    @Test
    void searchEmployees_ShouldReturnFilteredEmployees() {

        Pageable pageable = PageRequest.of(0,5);

        Page<Employee> page =
                new PageImpl<>(List.of(employee));

        when(employeeRepository.findAll(
                any(Specification.class),
                eq(pageable)))
                .thenReturn(page);

        Page<EmployeeResponse> response =
                employeeService.searchEmployees(
                        "IT",
                        EmployeeStatus.ACTIVE,
                        pageable
                );

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());

        verify(employeeRepository)
                .findAll(any(Specification.class), eq(pageable));
    }

}