package com.nhnacademy.aiot.gateway.Node;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.json.JSONObject;

import com.nhnacademy.aiot.gateway.Message.JsonMessage;

public class MqttInNode extends InputNode {
    private IMqttClient server;
    private MqttConnectOptions options;


    public MqttInNode() {
        //this(1);
    }

    public MqttInNode(String name) {
        super(name);
    }

    public void connectServer() {
        IMqttClient server;
        try {
            server = new MqttClient("tcp://ems.nhnacademy.com", super.getId().toString());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(1000);
            options.setWill("test/will", "Disconnected".getBytes(), 2, false);
            server.connect(options);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void serverSubscribe() {
        try {
            server.subscribe("#", (topic, msg) -> {
                JSONObject jsonObject = new JSONObject(msg);
                jsonObject.put("topic", topic);
                if (topic.contains("application")) {
                    JSONObject jsonPayload = new JSONObject(new String(msg.getPayload()));
                    jsonObject.put("payload", jsonPayload);
                    output(new JsonMessage(jsonObject));
                } else {
                    output(new JsonMessage(jsonObject));
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    void preprocess() {
        connectServer();
        serverSubscribe();
    }

    @Override
    void process() {
        if (!server.isConnected()) {
            serverSubscribe();
        }
    }

    @Override
    void postprocess() {
        
    }
}
