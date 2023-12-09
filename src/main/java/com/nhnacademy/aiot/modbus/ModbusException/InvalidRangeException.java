package com.nhnacademy.aiot.modbus.ModbusException;

public class InvalidRangeException extends RuntimeException {
    InvalidRangeException(String message) {    // 3, quantity와 value의 제한된 범위
        super(message);
    }
}