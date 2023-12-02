package com.nhnacademy.aiot.node;

import java.nio.charset.StandardCharsets;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;
import com.nhnacademy.aiot.message.JsonMessage;
import com.nhnacademy.aiot.message.Message;

public class MqttOutNode extends OutputNode {

    private IMqttClient toTelegraf;

    public MqttOutNode() {
        this("MqttOutNode", 1);
    }

    public MqttOutNode(String name, int cnt) {
        super(name, cnt);
    }

    void connectServer() {
        try {
            toTelegraf = new MqttClient("tcp://localhost", super.getId().toString());
            
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            options.setWill("test/will", "Disconnected".getBytes(), 1, true);
            // options.setKeepAliveInterval(1000);
            
            toTelegraf.connect();
        } catch (MqttException e) {
            log.info("{} : connectServer()메서드 {} 발생", getClass().getSimpleName(), e.getClass());
            e.printStackTrace();
        }
    }

    @Override
    void preprocess() {
        connectServer();
    }

    @Override
    void process() {
        if ((getInputWire(0) != null) && getInputWire(0).hasMessage()) {
            Message message = getInputWire(0).get();
            JSONObject object = ((JsonMessage) message).getPayload();

            try {
                String topic = object.getString("topic");
                JSONObject payload = object.getJSONObject("payload"); // new JSONObject로 한 번더 감싸지말기

                byte[] payloadBytes = payload.toString().getBytes(StandardCharsets.UTF_8);

                MqttMessage msg = new MqttMessage();
                msg.setPayload(payloadBytes);
                msg.setQos(2);
                msg.setRetained(true);
                toTelegraf.publish(topic, msg);
                
            } catch (JSONException | MqttException e) {
                log.info("{} : process()메서드 {} 발생", getClass().getSimpleName(), e.getClass());
            e.printStackTrace();
            }
        }
    }
}
