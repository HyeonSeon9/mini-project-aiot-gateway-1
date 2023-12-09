package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.modbus.client.Client;

public class ModbusReadNode extends InputNode {
    private String dataType;
    private int quantity;
    private String serverName;
    private int unitid;
    private Client sever;

    public String getServerName() {
        return serverName;
    }

    public ModbusReadNode(String name, int port) {
        super(name, port);
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
        this.sever = server;
    }

    public void setUnitId(int unitId) {
        this.unitid = unitId;
    }

    @Override
    void preprocess() {

    }

    @Override
    void process() {

    }

}
