package com.emsee.employeemanagement.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String username;

    private String role;

    private String accessToken;

    private String refreshToken;

    private String message;
}