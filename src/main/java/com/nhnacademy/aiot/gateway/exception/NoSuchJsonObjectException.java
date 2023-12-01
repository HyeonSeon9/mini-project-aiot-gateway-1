package com.nhnacademy.aiot.gateway.exception;

public class NoSuchJsonObjectException extends RuntimeException {

    public NoSuchJsonObjectException(String errorMessage) {
        super(errorMessage);
    }
}
