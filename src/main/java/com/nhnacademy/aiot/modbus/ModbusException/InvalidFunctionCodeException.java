package com.nhnacademy.aiot.modbus.ModbusException;

public class InvalidFunctionCodeException extends RuntimeException {
    InvalidFunctionCodeException(String message) {  // 1
        super(message);
    }
}
