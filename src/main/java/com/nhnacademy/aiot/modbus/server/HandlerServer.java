package com.nhnacademy.aiot.modbus.server;

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
    private int[] inputRegisters;

    public HandlerServer(Socket socket, int[] holdingRegisters, int[] inputRegisters) {
        this.socket = socket;
        this.holdingRegisters = holdingRegisters;
        this.inputRegisters = inputRegisters;
        this.thread = new Thread(this, this.getClass().getSimpleName());

        for (int i = 0; i < holdingRegisters.length; i++) {
            holdingRegisters[i] = i;
        }

        for (int i = 0; i < inputRegisters.length; i++) {
            inputRegisters[i] = 200;
        }

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
                byte[] receivedRequest = Arrays.copyOfRange(inputBuffer, 0, receivedLength);
                System.out.println(Arrays.toString(receivedRequest));

                if ((receivedLength > DEFAULT_LENGTH_HAS_UNIT_ID) && (DEFAULT_LENGTH
                        + readTwoByte(inputBuffer[4], inputBuffer[5])) == receivedLength) {

                    int transactionId = readTwoByte(inputBuffer[0], inputBuffer[1]);
                    int functionCode = inputBuffer[7];
                    int address = readTwoByte(inputBuffer[8], inputBuffer[9]);
                    int quantity = readTwoByte(inputBuffer[10], inputBuffer[11]);
                    System.out.println("+++++ "+quantity);
                    switch (functionCode) {
                        case 3:
                            if (address + quantity < holdingRegisters.length) {
                                System.out.println(
                                        "Address : " + address + ", Quantity: " + quantity);

                                byte[] response = SimpleMB.makeReadHoldingRegistersResponse(
                                        Arrays.copyOfRange(holdingRegisters, address,
                                                address + quantity));

                                byte[] addMBAP =
                                        SimpleMB.addMBAP(transactionId, inputBuffer[6], response);
                                outputStream.write(addMBAP);
                                outputStream.flush();
                            }
                            break;
                        
                        case 4:
                            if (address + quantity < holdingRegisters.length) {
                                byte[] response = SimpleMB.makeReadInputRegisterResponse(address, quantity, inputRegisters);

                                outputStream.write(SimpleMB.addMBAP(transactionId, inputBuffer[6], response));
                                outputStream.flush();
                            }
                            break;
                        
                        case 6:
                            holdingRegisters[address] = quantity;
                            
                            outputStream.write(receivedRequest);
                            outputStream.flush();

                            break;
                        
                        case 16:
                            if (address + quantity < holdingRegisters.length) {
                                int idx = address;

                                for (int i = 0; i < quantity; i++) {
                                    holdingRegisters[idx++] = readTwoByte(inputBuffer[13 + (i * 2)], inputBuffer[14 + (i * 2)]);
                                }

                                for (int i = address - 1; i < address + quantity + 1; i++) {
                                    System.out.println(holdingRegisters[i]);
                                }

                                byte[] response = SimpleMB.makeWriteMultipleRegistersResponse(address, quantity);

                                outputStream.write(SimpleMB.addMBAP(transactionId, inputBuffer[6],response));
                            }
                            break;

                        default:
                            
                            break;
                    }

                } else {
                    System.err.println("수신 패킷 길이가 잘못되었습니다.");
                }

            }
            socket.close();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
           HandlerServer a = new HandlerServer(null, null, null);
           int b = a.readTwoByte((byte)1, (byte)1);
           System.out.println(b);
    }

}
