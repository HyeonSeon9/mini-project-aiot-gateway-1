package com.nhnacademy.aiot.modbus.ModbusException;

public class InvalidLengthException extends RuntimeException {
    InvalidLengthException(String message) {        // 2, Address, Address + Quantity 범위
        super(message);
    }    
}