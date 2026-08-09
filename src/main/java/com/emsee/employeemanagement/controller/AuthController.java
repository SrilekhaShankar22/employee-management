package com.emsee.employeemanagement.controller;

import com.emsee.employeemanagement.dto.*;
import com.emsee.employeemanagement.entity.RefreshToken;
import com.emsee.employeemanagement.security.JwtService;
import com.emsee.employeemanagement.service.RefreshTokenService;
import com.emsee.employeemanagement.service.impl.CustomUserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.emsee.employeemanagement.repository.UserRepository;
import com.emsee.employeemanagement.entity.User;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        //Authentication, JWT generation & Authories/Role
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getUsername());

        //Save refresh token, Access database fields & Business
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken = jwtService.generateToken(userDetails);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return LoginResponse.builder()
                .username(user.getUsername())
                .role(user.getRole().name())
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .message("Login Successful")
                .build();
    }


    @PostMapping("/refresh")
    public RefreshTokenResponse refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {

        RefreshToken refreshToken = refreshTokenService
                //Find the refresh token
                .findByToken(request.getRefreshToken())
                //verify expiration
                .map(refreshTokenService::verifyExpiration)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        //Generate a new Access Token
        String accessToken = jwtService.generateToken(
                customUserDetailsService.loadUserByUsername(
                        refreshToken.getUser().getUsername()
                )
        );

        return RefreshTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .message("Access token refreshed successfully")
                .build();
    }


    @PostMapping("/logout")
    public String logout(@Valid @RequestBody LogoutRequest request) {

        refreshTokenService.deleteByToken(request.getRefreshToken());

        return "Logged out successfully";
    }
}