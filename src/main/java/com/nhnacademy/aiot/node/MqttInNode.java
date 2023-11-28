package com.nhnacademy.aiot.node;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONObject;
import com.nhnacademy.aiot.message.JsonMessage;

public class MqttInNode extends InputNode {
    private String[] args;
    private IMqttClient server;
    private MqttConnectOptions options;

    public MqttInNode() {
        this(1);
    }

    public MqttInNode(int count) {
        super(count);
    }

    public void setCommand(String[] args) {
        this.args = args;
    }

    @Override
    void preprocess() {
        try {
            server = new MqttClient("tcp://ems.nhnacademy.com", super.getId().toString());
            options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(1000);
            server.connect(options);
        } catch (MqttException e) {

        }
    }

    @Override
    void process() {
        try {
            server.subscribe("application/+/device/+/event/up", (topic, msg) -> {
                JSONObject payload = new JSONObject(new String(msg.getPayload()));
                output(new JsonMessage(payload.getJSONObject("deviceInfo").getJSONObject("tags")));
                // payload.getJSONObject("object").keySet().forEach(System.out::println);
            });
        } catch (MqttException e) {

        }
    }

    @Override
    void postprocess() {}

}
