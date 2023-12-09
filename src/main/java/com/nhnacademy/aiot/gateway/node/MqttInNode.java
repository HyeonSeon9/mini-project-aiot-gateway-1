package com.nhnacademy.aiot.gateway.node;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONObject;
import com.nhnacademy.aiot.gateway.message.JsonMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MqttInNode extends InputNode {
    private IMqttClient server = null;
    
    public MqttInNode() {
        this(1);
    }

    public MqttInNode(int count) {
        super(count);
    }

    public void connenctServer() {
        MqttConnectOptions options;
        try {
            server = new MqttClient("tcp://ems.nhnacademy.com", super.getId().toString());
            options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setKeepAliveInterval(1000);
            options.setWill("test/will", "Disconnected".getBytes(), 2, false);
            server.connect(options);
        } catch (MqttException e) {
            
        }
    }

    public void serverSubscribe() {
        try {
            server.subscribe("#", (topic, msg) -> {
                JSONObject object = new JSONObject(msg);
                object.put("topic", topic); 
                if (topic.contains("application")) {
                    JSONObject payload = new JSONObject(new String(msg.getPayload()));
                    object.put("payload", payload);
                    output(new JsonMessage(object));
                } else {
                    output(new JsonMessage(object));
                }

            });
        } catch(MqttException e) {
            log.error("Exception 발생 : {}", e);
        }
    }

    @Override
    void preprocess() {
        connenctServer();
        serverSubscribe();
    }

    @Override
    void process() {
        if (!server.isConnected()) {
            connenctServer();
            serverSubscribe();
        }
    }
}
