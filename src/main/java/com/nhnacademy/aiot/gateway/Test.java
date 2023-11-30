package com.nhnacademy.aiot.gateway;

import com.nhnacademy.aiot.node.ConcatNode;
import com.nhnacademy.aiot.node.MqttInNode;
import com.nhnacademy.aiot.node.MqttOutNode;
import com.nhnacademy.aiot.node.SetNode;
import com.nhnacademy.aiot.node.TopicNode;
import com.nhnacademy.aiot.wire.BufferedWire;
import com.nhnacademy.aiot.wire.Wire;

public class Test {

    public static void main(String[] args) {
        MqttInNode mqttIn = new MqttInNode();
        //TerminalOutNode out = new TerminalOutNode();
        SetNode set = new SetNode("set", 1,1, args);
        TopicNode topic = new TopicNode("topic",1,1);
        ConcatNode concat = new ConcatNode("concat", 1, 1);
        MqttOutNode mqttOut = new MqttOutNode("mqttOut");

        Wire wire1 = new BufferedWire();
        Wire wire2 = new BufferedWire();
        Wire wire3 = new BufferedWire();
        Wire wire4 = new BufferedWire();

        mqttIn.connectOutputWire(0, wire1);
        set.connectInputWire(0, wire1);

        set.connectOutputWire(0, wire2);
        topic.connectInputWire(0, wire2);
        
        topic.connectOutputWire(0, wire3);
        concat.connectInputWire(0, wire3);

        concat.connectOutputWire(0, wire4);
        mqttOut.connectInputWire(0, wire4);
        //out.connectInputWire(0, wire);

        mqttIn.start();
        set.start();
        topic.start();
        concat.start();
        mqttOut.start();
        //out.start();
    }
}
