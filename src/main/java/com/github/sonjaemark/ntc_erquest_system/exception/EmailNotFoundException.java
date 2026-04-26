package com.github.sonjaemark.ntc_erquest_system.exception;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.AuthenticationException;
public class EmailNotFoundException extends AuthenticationException{

    public EmailNotFoundException(@Nullable String msg) {
        super(msg);
    }
}

