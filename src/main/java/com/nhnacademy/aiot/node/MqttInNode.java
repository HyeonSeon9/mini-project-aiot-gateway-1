package com.nhnacademy.aiot.node;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONObject;
import com.nhnacademy.aiot.message.JsonMessage;

public class MqttInNode extends InputNode {
    private IMqttClient server = null;
    private MqttConnectOptions options;


    public MqttInNode() {
        this(1);
    }

    public MqttInNode(String name) {
        super(name);
    }

    public MqttInNode(int count) {
        super(count);
    }



    public void connectServer() {
        try {
            server = new MqttClient("tcp://ems.nhnacademy.com", super.getId().toString());
            options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(1000);
            options.setWill("test/will", "Disconnected".getBytes(), 2, false);
            server.connect(options);
        } catch (MqttException e) {

        }
    }

    public void serverSubscribe() {
        try {
            server.subscribe("#", (topic, msg) -> {
                JSONObject jsonObject = new JSONObject(msg);
                jsonObject.put("topic", topic);
                if (topic.contains("application")) {
                    JSONObject jsonPayLoad = new JSONObject(new String(msg.getPayload()));
                    jsonObject.put("payload", jsonPayLoad);
                    output(new JsonMessage(jsonObject));
                } else {
                    output(new JsonMessage(jsonObject));
                }
            });
        } catch (MqttException e) {
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
    void postprocess() {}

}
