package com.nhnacademy.aiot.modbus.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Handle implements Runnable {


    private Socket socket;

    int[] holdingRegister;
    int[] inputResgister;
    private int unitId = 1;

    public Handle(Socket socket, int[] holdingRegister, int[] inputRegister) {
        this.socket = socket;
        this.holdingRegister = holdingRegister;
        this.inputResgister = inputRegister;
        Thread thread = new Thread(this, this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        try (BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
                BufferedOutputStream outputStream =
                        new BufferedOutputStream(socket.getOutputStream());) {


            byte[] inputBuffer = new byte[1024];

            int receivedLength = inputStream.read(inputBuffer, 0, inputBuffer.length);

            if (receivedLength > 0) {
                log.info(Arrays.toString(Arrays.copyOfRange(inputBuffer, 0, receivedLength)));
                if ((receivedLength > 7) && (6 + inputBuffer[5]) == receivedLength) {
                    if (unitId == inputBuffer[6]) {
                        int transactionId = (inputBuffer[0] << 8) | inputBuffer[1];
                        int functionCode = inputBuffer[7];

                        switch (functionCode) {
                            case 3:
                                int address = (inputBuffer[8] << 8) | inputBuffer[9];
                                int quantity = (inputBuffer[10] << 8) | inputBuffer[11];

                                if (address + quantity < holdingRegister.length) {
                                    System.out.println(
                                            "Address : " + address + ", Quantity: " + quantity);

                                    outputStream.write(SimpleMB.addMBAP(transactionId, unitId,
                                            SimpleMB.makeReadHoldingRegistersResponse(
                                                    address, Arrays.copyOfRange(holdingRegister,
                                                            address, quantity + address),
                                                    quantity)));
                                    outputStream.flush();
                                }
                                break;
                            case 4:
                                address = (inputBuffer[8] << 8) | inputBuffer[9];
                                quantity = (inputBuffer[10] << 8) | inputBuffer[11];

                                outputStream.write(SimpleMB.addMBAP(transactionId, unitId,
                                        SimpleMB.makeReadInputRegistersResponse(address,
                                                inputResgister, quantity)));
                                outputStream.flush();
                                break;

                            case 6:
                                address = (inputBuffer[8] << 8) | inputBuffer[9];
                                int value = 9;

                                outputStream.write(SimpleMB.addMBAP(transactionId, unitId,
                                        SimpleMB.makeSingleRegisterWriteResponse(address, value)));
                                outputStream.flush();

                                break;

                            case 16:
                                address = (inputBuffer[8] << 8) | inputBuffer[9];
                                quantity = (inputBuffer[10] << 8) | inputBuffer[11];

                                for (int i = 0; i < quantity; i++) {
                                    holdingRegister[address++] = (inputBuffer[13 + i * 2] << 8)
                                            | (inputBuffer[14 + i * 2] & 0xFF);
                                }
                                outputStream.write(SimpleMB.addMBAP(transactionId, unitId, SimpleMB
                                        .makeWriteMultipleregistersResponse(address, quantity)));
                                outputStream.flush();
                                break;
                            default:
                        }
                    }
                } else {
                    System.err.println("수신 패킷 길이가 잘못되었습니다.");
                }

            } else if (receivedLength < 0) {
            }



        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }



}
