package com.emsee.employeemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {// <T> used here is generics

    private boolean success;

    private String message;

    private T data;

    private Object errors;//delared in object because diffecrent APIs may return different kind of error details
}