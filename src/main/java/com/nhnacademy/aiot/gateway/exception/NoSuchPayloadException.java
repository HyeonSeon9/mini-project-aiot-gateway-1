package com.nhnacademy.aiot.gateway.exception;

public class NoSuchPayloadException extends RuntimeException {
    
    public NoSuchPayloadException(String errorMessage) {
        super(errorMessage);
    }
}
