package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.message.ByteMessage;
import com.nhnacademy.aiot.modbus.client.Client;

public class ModbusWriteNode extends InputOutputNode {
    private String dataType;
    private int quantity;
    private String serverName;
    private int unitid;
    private Client server;
    private static byte count = 0;

    public ModbusWriteNode(String name, int count) {
        super(name, 1, count);
    }

    public String getServerName() {
        return serverName;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setServer(Client server) {
        this.server = server;
    }

    public void setUnitId(int unitId) {
        this.unitid = unitId;
    }

    @Override
    void preprocess() {

    }

    @Override
    void process() {
        byte[] receive = null;
        byte[] bufferOut = new byte[] {0, 1, 0, 0, 0, 6, 1, 6, 0, count++, 0, 5};
        receive = server.sendAndReceive(bufferOut);
        output(new ByteMessage(receive));
    }

}
