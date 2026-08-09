package com.emsee.employeemanagement.service;

import com.emsee.employeemanagement.entity.RefreshToken;
import com.emsee.employeemanagement.entity.User;

import java.util.Optional;

public interface RefreshTokenService {

    //When user login we will Generate UUID, Set expiry, Link user, Save in DB
    RefreshToken createRefreshToken(User user);

    //When the page is refresheed we need to find in DB does it have the token
    Optional<RefreshToken> findByToken(String token);

    //it will comapre the current time and expiry date from DB if expired, delete the refresh token and throw an error
    RefreshToken verifyExpiration(RefreshToken token);

    //If the user loggedout, delete the refresh token
    void deleteByUser(User user);

    void deleteByToken(String token);
}
