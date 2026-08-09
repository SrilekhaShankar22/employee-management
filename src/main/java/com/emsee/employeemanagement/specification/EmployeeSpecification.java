package com.emsee.employeemanagement.specification;

import com.emsee.employeemanagement.entity.Employee;
import com.emsee.employeemanagement.enums.EmployeeStatus;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecification {

    public static Specification<Employee> hasDepartment(String department) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("department"), department);
    }

    public static Specification<Employee> hasStatus(EmployeeStatus status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }




}
