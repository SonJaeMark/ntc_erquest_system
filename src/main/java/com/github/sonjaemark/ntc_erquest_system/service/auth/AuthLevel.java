package com.github.sonjaemark.ntc_erquest_system.service.auth;

import java.util.List;

import com.github.sonjaemark.ntc_erquest_system.model.enums.UserRole;

public abstract class AuthLevel {
    protected final AuthService authService;

    protected AuthLevel(AuthService authService){
        this.authService = authService;
    }

    public Long  isAuthorized(List<UserRole> roles){
        return authService.getAuthorizedUser(roles).getId();
    }
}
