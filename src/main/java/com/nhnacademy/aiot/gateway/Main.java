package com.nhnacademy.aiot.gateway;


import com.nhnacademy.aiot.node.MqttInNode;
import com.nhnacademy.aiot.node.MqttOutNode;
import com.nhnacademy.aiot.node.SettingTopic;
import com.nhnacademy.aiot.node.SplitNode;
import com.nhnacademy.aiot.node.TransKorea;
import com.nhnacademy.aiot.wire.BufferedWire;
import com.nhnacademy.aiot.wire.Wire;

public class Main {
    public static void main(String[] args) {
        // MqttInNode mqin = new MqttInNode();
        // SplitNode split = new SplitNode("splitNode");
        // SettingTopic topic = new SettingTopic("topic");
        // TransKorea korea = new TransKorea("korea");
        // MqttOutNode mqttOut = new MqttOutNode("mqttOut", 1);
        // Wire wire1 = new BufferedWire();
        // Wire wire2 = new BufferedWire();
        // Wire wire3 = new BufferedWire();
        // Wire wire4 = new BufferedWire();

        // split.setCommand(args);

        // mqin.connectOutputWire(0, wire1);
        // split.connectInputWire(0, wire1);
        // split.connectOutputWire(0, wire2);
        // topic.connectInputWire(0, wire2);
        // topic.connectOutputWire(0, wire3);
        // korea.connectInputWire(0, wire3);
        // korea.connectOutputWire(0, wire4);
        // mqttOut.connectInputWire(0, wire4);

        // mqin.start();
        // split.start();
        // topic.start();
        // korea.start();
        // mqttOut.start();
    }
}
