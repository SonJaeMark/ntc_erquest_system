package com.github.sonjaemark.ntc_erquest_system.exception;

public class AccessTokenBlockedException extends RuntimeException{
    public AccessTokenBlockedException(String message) {
        super(message);
    }
}

