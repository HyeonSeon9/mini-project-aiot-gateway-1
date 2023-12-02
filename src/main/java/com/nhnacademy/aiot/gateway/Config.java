package com.nhnacademy.aiot.gateway;

public class Config {
    public static void main(String[] args) {
        NodeConfig config = new NodeConfig();
        
        config.getClassName();
        config.makeInstance();
        config.connectWire();
        config.getSettingFromCommandLine(args);
        config.nodeStart();
    }
}
