package com.github.sonjaemark.ntc_erquest_system.exception;

import org.springframework.security.core.AuthenticationException;

public class EmailAlreadyExistException extends AuthenticationException {
    public EmailAlreadyExistException(String message) {
        super(message);
    }
}