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
        // MqttInNode mqttIn = new MqttInNode("mqttIn", 1);
        // SetNode set = new SetNode("set", 1);
        // TopicNode topic = new TopicNode("topic",1,1);
        // ConcatNode concat = new ConcatNode("concat", 1, 1);
        // MqttOutNode mqttOut = new MqttOutNode("mqttOut", 1);

        // Wire wire1 = new BufferedWire();
        // Wire wire2 = new BufferedWire();
        // Wire wire3 = new BufferedWire();
        // Wire wire4 = new BufferedWire();

        // mqttIn.connectOutputWire(0, wire1);
        // set.connectInputWire(0, wire1);
        
        // // mqttIn.connectOutputWire(1, wire6);
        // // newNode.connectInputWire(0, wire6);

        // set.connectOutputWire(0, wire2);
        // topic.connectInputWire(0, wire2);
        
        // topic.connectOutputWire(0, wire3);
        // concat.connectInputWire(0, wire3);

        // concat.connectOutputWire(0, wire4);
        // mqttOut.connectInputWire(0, wire4);

        // mqttIn.start();
        // set.start();
        // topic.start();
        // concat.start();
        // mqttOut.start();
    }
}
