package com.nhnacademy.aiot.modbus.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class modbusServer {
    private static int[] holdingRegister = new int[100];
    private static int[] inputRegister = new int[100];
    private static final int PORT = 11502;
    

    public static void main(String[] args) {
        Socket socket;

        try (ServerSocket server = new ServerSocket(PORT);) {

            while ((socket = server.accept()) != null) {
                log.info(socket.getClass().getSimpleName() + "연결 완료");

                Handle handle = new Handle(socket, holdingRegister, inputRegister);
                handle.run();
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


}
