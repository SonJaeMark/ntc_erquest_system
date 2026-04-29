package com.github.sonjaemark.ntc_erquest_system.exception;

public class DocumentNotFoundException extends RuntimeException{
    public DocumentNotFoundException(String message){
        super(message);
    }
}
