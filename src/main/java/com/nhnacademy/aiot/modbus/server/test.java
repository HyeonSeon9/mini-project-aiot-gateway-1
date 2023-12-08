package com.nhnacademy.aiot.modbus.server;

import java.io.IOException;
import com.nhnacademy.aiot.gateway.SettingNode;

public class test {
    public static void main(String[] args) throws IOException {
        // ModbusServerNode server = new ModbusServerNode("modbus", 1);
        // server.launchServer(11502, 100, 100);
        // server.start();
        SettingNode settingNode = new SettingNode();
        settingNode.makeFlow();
    }
}
