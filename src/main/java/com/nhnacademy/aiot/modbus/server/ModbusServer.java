package com.nhnacademy.aiot.modbus.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModbusServer {
    private static int[] holdingRegisters = new int[100];
    private static int[] inputRegisters = new int[100];
    private static final int PORT = 11502;



    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PORT);) {
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                HandlerServer handlerServer =
                        new HandlerServer(socket, holdingRegisters, inputRegisters);
                handlerServer.start();

                log.info("{} | PORT : {}", socket.getInetAddress().getHostAddress(),
                        socket.getPort());
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}


