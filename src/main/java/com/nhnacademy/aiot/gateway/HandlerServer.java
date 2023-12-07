package com.nhnacademy.aiot.gateway;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class HandlerServer implements Runnable {
    private final Socket socket;
    private final Thread thread;


    private static final int DEFAULT_LENGTH_HAS_UNIT_ID = 7;
    private static final int DEFAULT_LENGTH = 6;

    private int[] holdingRegisters;

    public HandlerServer(Socket socket, int[] holdingRegisters) {
        this.socket = socket;
        this.holdingRegisters = holdingRegisters;
        this.thread = new Thread(this, this.getClass().getSimpleName());
    }

    public int readTwoByte(byte first, byte second) {
        return ((first << 8) & 0xFF00 | second & 0x00FF);
    }

    void start() {
        thread.start();
    }

    @Override
    public void run() {
        try (BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
                BufferedOutputStream outputStream =
                        new BufferedOutputStream(socket.getOutputStream());) {

            byte[] inputBuffer = new byte[1024];
            int receivedLength = inputStream.read(inputBuffer, 0, inputBuffer.length);

            if (receivedLength > 0) {
                System.out.println(
                        Arrays.toString(Arrays.copyOfRange(inputBuffer, 0, receivedLength)));

                if ((receivedLength > DEFAULT_LENGTH_HAS_UNIT_ID) && (DEFAULT_LENGTH
                        + readTwoByte(inputBuffer[4], inputBuffer[5])) == receivedLength) {

                    int transactionId = readTwoByte(inputBuffer[0], inputBuffer[1]);
                    int functionCode = inputBuffer[7];

                    switch (functionCode) {
                        case 3:
                            int address = readTwoByte(inputBuffer[8], inputBuffer[9]);
                            int quantity = readTwoByte(inputBuffer[10], inputBuffer[11]);

                            if (address + quantity < holdingRegisters.length) {
                                System.out.println(
                                        "Address : " + address + ", Quantity: " + quantity);

                                outputStream.write(SimpleMB.addMBAP(transactionId, inputBuffer[6],
                                        SimpleMB.makeReadHoldingRegistersResponse(address,
                                                Arrays.copyOfRange(holdingRegisters, address,
                                                        quantity))));
                                outputStream.flush();
                            }

                            break;

                        default:
                            break;
                    }

                } else {
                    System.err.println("수신 패킷 길이가 잘못되었습니다.");
                }

            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }


}
