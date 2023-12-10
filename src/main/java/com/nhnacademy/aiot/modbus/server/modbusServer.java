package com.nhnacademy.aiot.modbus.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class modbusServer {
    private static int[] holdingRegister = new int[100];
    private static int[] inputRegister = new int[100];
    private static final int port = 11502;
    private static Socket socket;

    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(port);) {
            while ((socket = server.accept()) != null) {
                Handle handle = new Handle(socket, holdingRegister, inputRegister);
                handle.run();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


}
