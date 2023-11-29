package com.nhnacademy.aiot.gateway;

import com.nhnacademy.aiot.node.MqttInNode;
import com.nhnacademy.aiot.node.SetNode;
import com.nhnacademy.aiot.node.TerminalOutNode;
import com.nhnacademy.aiot.node.TopicNode;
import com.nhnacademy.aiot.wire.BufferedWire;
import com.nhnacademy.aiot.wire.Wire;

public class Test {
    public static void main(String[] args) {
        MqttInNode mqttIn = new MqttInNode();
        //TerminalOutNode out = new TerminalOutNode();
        SetNode set = new SetNode("set", 1,1, args);
        TopicNode topic = new TopicNode("topic",1,1);

        Wire wire1 = new BufferedWire();
        Wire wire2 = new BufferedWire();

        mqttIn.connectOutputWire(0, wire1);
        set.connectInputWire(0, wire1);
        set.connectOutputWire(0, wire2);
        topic.connectInputWire(0, wire2);
        //out.connectInputWire(0, wire);

        mqttIn.start();
        set.start();
        topic.start();
        //out.start();
    }
}
