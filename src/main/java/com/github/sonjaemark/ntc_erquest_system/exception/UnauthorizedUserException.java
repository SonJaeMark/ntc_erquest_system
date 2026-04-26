package com.github.sonjaemark.ntc_erquest_system.exception;

import org.springframework.security.core.AuthenticationException;

public class UnauthorizedUserException extends AuthenticationException {
    public UnauthorizedUserException(String message) {
        super(message);
    }
}
