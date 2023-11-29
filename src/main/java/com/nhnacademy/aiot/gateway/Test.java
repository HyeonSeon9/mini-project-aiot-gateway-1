package com.nhnacademy.aiot.gateway;

import com.nhnacademy.aiot.node.MqttInNode;
import com.nhnacademy.aiot.node.MqttOutNode;
import com.nhnacademy.aiot.node.PlaceTranslatorNode;
import com.nhnacademy.aiot.node.ReduceTopicNode;
import com.nhnacademy.aiot.node.SplitNode;
import com.nhnacademy.aiot.wire.BufferedWire;
import com.nhnacademy.aiot.wire.Wire;

public class Test {
    public static void main(String[] args) {
        MqttInNode mqttIn = new MqttInNode();
        SplitNode split = new SplitNode("splitNode");
        PlaceTranslatorNode trans = new PlaceTranslatorNode("TransNode");
        ReduceTopicNode reduce = new ReduceTopicNode("reduceNode");
        MqttOutNode mqttOut = new MqttOutNode();

        Wire wire = new BufferedWire();
        Wire wire1 = new BufferedWire();
        Wire wire2 = new BufferedWire();
        Wire wire3 = new BufferedWire();

        split.setCommand(args);

        mqttIn.connectOutputWire(0, wire);
        split.connectInputWire(0, wire);

        split.connectOutputWire(0, wire1);
        trans.connectInputWire(0, wire1);

        trans.connectOutputWire(0, wire2);
        reduce.connectInputWire(0, wire2);

        reduce.connectOutputWire(0, wire3);
        mqttOut.connectInputWire(0, wire3);

        mqttIn.start();
        split.start();
        trans.start();
        reduce.start();
        mqttOut.start();
    }
}
