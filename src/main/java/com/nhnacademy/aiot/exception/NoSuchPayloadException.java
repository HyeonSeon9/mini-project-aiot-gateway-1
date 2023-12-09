package com.nhnacademy.aiot.exception;

public class NoSuchPayloadException extends RuntimeException {
    
    public NoSuchPayloadException(String errorMessage) {
        super(errorMessage);
    }
}
