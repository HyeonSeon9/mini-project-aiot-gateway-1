package com.nhnacademy.aiot.modbus.client;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private String host;
    private String name;
    private int port;
    private int unitId;

    public Client(String name, String host, int port, int unitId) {
        this.host = host;
        this.name = name;
        this.port = port;
        this.unitId = unitId;
    }


    public void send(byte[] request) throws IOException {

        try (Socket socket = new Socket(host, port);
                BufferedOutputStream outputStream =
                        new BufferedOutputStream(socket.getOutputStream())) {
            outputStream.write(request);
            outputStream.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public int getUnitId() {
        return unitId;
    }

    public static int[] addByte(byte[] inputByte) {
        int byteCount = inputByte[8];
        int[] result = new int[byteCount / 2];
        for (int i = 0; i < result.length; i++) {
            result[i] = readTwoByte(inputByte[9 + i * 2], inputByte[9 + i * 2 + 1]);
        }
        return result;
    }

    public static int readTwoByte(byte first, byte second) {
        return ((first << 8) & 0xFF00 | second & 0x00FF);
    }

    // public static void main(String[] args) {
    // try (Socket socket = new Socket(host, port);
    // BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
    // BufferedOutputStream outputStream =
    // new BufferedOutputStream(socket.getOutputStream());) {

    // byte[] request = new byte[] {0, 1, 0, 0, 0, 6, 1, 3, 0, 0, 0, 10};

    // outputStream.write(request);
    // outputStream.flush();

    // byte[] inputBuffer = new byte[1024];
    // int receivedLength = inputStream.read(inputBuffer, 0, inputBuffer.length);
    // byte[] inputByte = Arrays.copyOfRange(inputBuffer, 0, receivedLength);

    // System.out.println(Arrays.toString(inputByte));

    // if (receivedLength > 0) {
    // System.out.println(Arrays.toString(addByte(inputByte)));
    // }
    // } catch (IOException e) {
    // System.err.println(e.getMessage() + " | 연결에 실패하였습니다.");
    // }
    // }
}
