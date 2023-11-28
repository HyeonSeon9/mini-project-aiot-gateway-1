package com.nhnacademy.aiot.gateway;

import com.nhnacademy.aiot.node.MqttInNode;

public class Test {
    public static void main(String[] args) {
        MqttInNode mqttIn = new MqttInNode();
        mqttIn.start();
    }
}
