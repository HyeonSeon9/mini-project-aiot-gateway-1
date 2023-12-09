package com.nhnacademy.aiot.node;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModBusServerNode extends OutputNode {

    ModBusServerNode(int count) {
        super(count);
    }

    public void connectServer() {

        try (ServerSocket server = new ServerSocket(11502); Socket socket = server.accept()) {
            System.out.println(socket.getClass().getSimpleName() + "연결 완료");

            Thread clientThread = new Thread(() -> handler(socket));
            clientThread.start();

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void handler(Socket socket) {

        try (BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
                BufferedOutputStream outputStream =
                        new BufferedOutputStream(socket.getOutputStream());) {

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
