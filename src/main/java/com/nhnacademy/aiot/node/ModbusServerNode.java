package com.nhnacademy.aiot.node;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.json.JSONObject;
import com.nhnacademy.aiot.message.JsonMessage;
import com.nhnacademy.aiot.message.Message;
import com.nhnacademy.aiot.modbus.server.HandlerServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModbusServerNode extends InputOutputNode {

    private int[] holdingRegisters;
    private int[] inputRegisters;

    private ServerSocket serverSocket;
    private Socket socket;

    public ModbusServerNode(String name, int count) {
        super(name, 1, count);

    }

    public void launchServer(int port, int holdingBufferSize, int inputBufferSize) {
        try {
            this.serverSocket = new ServerSocket(port);
            this.holdingRegisters = new int[holdingBufferSize];
            this.inputRegisters = new int[inputBufferSize];
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    void process() {
        try {
            if ((socket = serverSocket.accept()) != null) {
                HandlerServer handlerServer =
                        new HandlerServer(socket, holdingRegisters, inputRegisters);
                handlerServer.start();
                log.info("{} | PORT : {}", socket.getInetAddress().getHostAddress(),
                        socket.getPort());
            }
        } catch (IOException e) {

        }
        if (((getInputWire(0) != null) && (getInputWire(0).hasMessage()))) {
            Message message = getInputWire(0).get();
            JSONObject jsonObject = new JSONObject(((JsonMessage) message).getPayload().toString());
        }
    }
}
