package com.github.sonjaemark.ntc_erquest_system.exception;

public class DocumentRequestAlreadyExistException extends RuntimeException{
    public DocumentRequestAlreadyExistException(String message){
        super(message);
    }
}
