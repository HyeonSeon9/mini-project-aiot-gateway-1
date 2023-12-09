package com.nhnacademy.aiot.exception;

public class NoSuchJsonObjectException extends RuntimeException {

    public NoSuchJsonObjectException(String errorMessage) {
        super(errorMessage);
    }
}
