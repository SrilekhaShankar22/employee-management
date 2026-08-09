package com.emsee.employeemanagement.repository;

import com.emsee.employeemanagement.entity.Employee;
import com.emsee.employeemanagement.enums.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    //commented the query as we can go with JpaSpecificationExecuter
    //Page<Employee> findByDepartment(String department, Pageable pageable);

//    Page<Employee> findByDepartmentAndStatus(
//            String department,
//            EmployeeStatus status,
//            Pageable pageable
//    );
}