package com.nhnacademy.aiot.modbus.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Client {
    private String host;
    private String name;
    private int port;
    private int unitId;


    public Client(String host, String name, int port, int unitId) {
        this.host = host;
        this.name = name;
        this.port = port;
        this.unitId = unitId;
    }

    public byte[] sendAndReceive(byte[] request) {
        byte[] inputBuffer = null;
        try (Socket socket = new Socket(host, port);
                BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
                BufferedOutputStream outputStream =
                        new BufferedOutputStream(socket.getOutputStream());) {
            outputStream.write(request);
            outputStream.flush();

            byte[] inputTemp = new byte[1024];
            int inputBufferedLenghth = inputStream.read(inputTemp, 0, inputTemp.length);

            inputBuffer = Arrays.copyOfRange(inputTemp, 0, inputBufferedLenghth);
        } catch (IOException e) {
            log.info(e.getMessage());
        }

        return inputBuffer;
    }

    public String getName() {
        return this.name;
    }

    public int getUnitId() {
        return this.unitId;
    }

    public static void main(String[] args) {
        Client c = new Client("test", "Client", 11502, 1);
        byte[] temp = {0, 1, 0, 0, 0, 6, 1, 3, 0, 1, 0, 5};
        System.out.println(c.sendAndReceive(temp));
    }
}
