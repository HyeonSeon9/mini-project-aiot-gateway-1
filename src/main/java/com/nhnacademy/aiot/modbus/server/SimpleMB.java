package com.nhnacademy.aiot.modbus.server;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class SimpleMB {
    public static byte[] makeReadHoldingRegistersRequest(int address, int quantity) {
        byte[] frame = new byte[5]; // 5바이트인이유는 리퀘스트가 총 5 바이트이기때음
        // 0000 0000 0000 0000 0000 0000 0000 0000 0000 00000
        //
        ByteBuffer b = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
        // 0000 0000 0000 0000 0000 0000 0000 0101
        // PDU의 function code
        frame[0] = 0x03;

        // PDU의 data
        b.putInt(address);
        frame[1] = b.get(2);
        frame[2] = b.get(3);

        b.clear();
        b.putInt(quantity);
        frame[3] = b.get(2);
        frame[4] = b.get(3);

        return frame;
    }

    public static byte[] makeReadHoldingRegistersResponse(int address, int[] registers,
            int quantity) {
        byte[] frame = new byte[1 + 1 + quantity * 2];

        // PDU의 Function Code
        frame[0] = 0x03;

        // Length
        frame[1] = (byte) (quantity * 2);
        int index = 0;
        for (int i = 0; i < quantity; i++) {
            frame[2 + i * 2] = (byte) ((registers[index] >> 8) & 0xFF);
            frame[2 + i * 2 + 1] = (byte) (registers[index++] & 0xFF);

        }

        return frame;
    }

    public static byte[] makeReadInputRegistersResponse(int address, int[] registers,
            int quantity) {
        byte[] frame = new byte[2 + quantity * 2];

        frame[0] = 0x04;
        frame[1] = (byte) (quantity * 2);

        for (int i = 0; i < quantity; i++) {
            frame[2 + i * 2] = (byte) ((registers[address] >> 8) & 0xFF);
            frame[3 + i * 2] = (byte) ((registers[address++]) & 0xFF);

        }
        return frame;
    }

    public static byte[] makeSingleRegisterWriteResponse(int address, int value) {
        byte[] frame = new byte[5]; // function 1 , address 2, value 2
        frame[0] = 0x06;
        ByteBuffer b = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);

        b.putInt(address);
        frame[1] = b.get(2);
        frame[2] = b.get(3);
        b.clear();
        b.putInt(value);
        frame[3] = b.get(2);
        frame[4] = b.get(3);

        return frame;
    }

    public static byte[] makeWriteMultipleregistersResponse(int address, int quantity) {
        byte[] frame = new byte[5]; // function 1 , address 2, quantity 2
        frame[0] = 0x10;
        ByteBuffer b = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
        b.putInt(address);
        frame[1] = b.get(2);
        frame[2] = b.get(3);
        b.clear();
        b.putInt(quantity);
        frame[3] = b.get(2);
        frame[4] = b.get(3);


        return frame;
    }


    public static byte[] addMBAP(int transactionId, int unitId, byte[] pdu) {
        byte[] adu = new byte[7 + pdu.length];
        ByteBuffer b = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);

        b.putInt(transactionId);

        adu[0] = b.get(2);
        adu[1] = b.get(3);
        adu[2] = 0;
        adu[3] = 0;
        adu[4] = 0;
        adu[5] = (byte) (pdu.length + 1); // unit ID 고려
        adu[6] = (byte) unitId;
        System.arraycopy(pdu, 0, adu, 7, pdu.length);

        return adu;
    }
}
