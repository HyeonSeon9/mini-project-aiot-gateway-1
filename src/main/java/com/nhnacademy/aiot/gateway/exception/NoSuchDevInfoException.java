package com.nhnacademy.aiot.gateway.exception;

public class NoSuchDevInfoException extends RuntimeException {

    public NoSuchDevInfoException(String errorMessage) {
        super(errorMessage);
    }
}
