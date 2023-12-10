package com.nhnacademy.aiot.node;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import com.nhnacademy.aiot.modbus.server.SimpleMB;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModBusServerNode extends InputOutputNode {

    private ServerSocket server;
    private static int[] holdingRegisters;
    private static int[] inputRegisters;
    private static int unitId = 1;

    ModBusServerNode(String name, int outCount) {
        super(name, 1, outCount);
    }

    public void connectServer(int port, int holdingRegistersSize, int inputRegistersSize) {

        try {
            this.server = new ServerSocket(port);
            Socket socket = server.accept();
            if (socket != null) {
                System.out.println(socket.getClass().getSimpleName() + "연결 완료");
                Thread clientThread =
                        new Thread(() -> handler(socket, holdingRegistersSize, inputRegistersSize));
                clientThread.start();
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void handler(Socket socket, int holdingRegistersSize, int inputRegistersSize) {

        try (BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
                BufferedOutputStream outputStream =
                        new BufferedOutputStream(socket.getOutputStream());) {
            while (socket.isConnected()) {
                holdingRegisters = new int[holdingRegistersSize];
                inputRegisters = new int[inputRegistersSize];

                byte[] inputBuffer = new byte[1024];

                int receivedLength = inputStream.read(inputBuffer, 0, inputBuffer.length);

                if (receivedLength > 0) {
                    System.out.println(
                            Arrays.toString(Arrays.copyOfRange(inputBuffer, 0, receivedLength)));

                    if ((receivedLength > 7) && (6 + inputBuffer[5]) == receivedLength) {
                        if (unitId == inputBuffer[6]) {
                            int transactionId = (inputBuffer[0] << 8) | inputBuffer[1];
                            int functionCode = inputBuffer[7];

                            switch (functionCode) {
                                case 3:
                                    int address = (inputBuffer[8] << 8) | inputBuffer[9];
                                    int quantity = (inputBuffer[10] << 8) | inputBuffer[11];

                                    if (address + quantity < holdingRegisters.length) {
                                        System.out.println(
                                                "Address : " + address + ", Quantity: " + quantity);

                                        outputStream.write(SimpleMB.addMBAP(transactionId, unitId,
                                                SimpleMB.makeReadHoldingRegistersResponse(address,
                                                        Arrays.copyOfRange(holdingRegisters,
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
                                                    inputRegisters, quantity)));
                                    outputStream.flush();
                                    break;

                                case 6:
                                    address = (inputBuffer[8] << 8) | inputBuffer[9];
                                    int value = 9;

                                    outputStream.write(SimpleMB.addMBAP(transactionId, unitId,
                                            SimpleMB.makeSingleRegisterWriteResponse(address,
                                                    value)));
                                    outputStream.flush();

                                    break;

                                case 16:
                                    address = (inputBuffer[8] << 8) | inputBuffer[9];
                                    quantity = (inputBuffer[10] << 8) | inputBuffer[11];

                                    for (int i = 0; i < quantity; i++) {
                                        holdingRegisters[address++] = (inputBuffer[13 + i * 2] << 8)
                                                | (inputBuffer[14 + i * 2] & 0xFF);
                                    }
                                    outputStream.write(SimpleMB.addMBAP(transactionId, unitId,
                                            SimpleMB.makeWriteMultipleregistersResponse(address,
                                                    quantity)));
                                    outputStream.flush();
                                    break;
                                default:
                            }
                        }
                    } else {
                        System.err.println("수신 패킷 길이가 잘못되었습니다.");
                    }

                } else if (receivedLength < 0) {
                    break;
                }



            }


        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    public static void main(String[] args) {
        ModBusServerNode serverNode = new ModBusServerNode("modebusServer", 1);
        serverNode.connectServer(11502, 100, 100);
    }
}
