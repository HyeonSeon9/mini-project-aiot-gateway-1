package com.nhnacademy.aiot.modbus.ModbusException;

public class InvalidDataAccessException extends RuntimeException {
    InvalidDataAccessException(String message) {    // 4, data 읽거나 저장할 때 
        super(message);
    }
}
