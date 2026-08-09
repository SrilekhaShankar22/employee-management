package com.emsee.employeemanagement.repository;

import com.emsee.employeemanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    //Used optional to handle both if user exist/ not, wont give null pointr exception
    Optional<User> findByUsername(String username);

}