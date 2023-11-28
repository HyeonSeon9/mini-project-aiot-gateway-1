package com.nhnacademy.aiot.gateway;

import com.nhnacademy.aiot.node.MqttInNode;
import com.nhnacademy.aiot.node.TerminalOutNode;
import com.nhnacademy.aiot.wire.BufferedWire;
import com.nhnacademy.aiot.wire.Wire;

public class Test {
    public static void main(String[] args) {
        MqttInNode mqttIn = new MqttInNode();
        TerminalOutNode out = new TerminalOutNode();
        Wire wire = new BufferedWire();

        mqttIn.connectOutputWire(0, wire);
        out.connectInputWire(0, wire);

        mqttIn.start();
        out.start();
    }
}
