package com.nhnacademy.aiot.node;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import com.github.f4b6a3.uuid.UuidCreator;
import com.nhnacademy.aiot.message.Message;
import lombok.extern.slf4j.Slf4j;
import com.nhnacademy.aiot.message.JsonMessage;

@Slf4j
public class MqttOutNode extends OutputNode {
    private IMqttClient local = null;


    public MqttOutNode() {
        super(1);
    }

    MqttOutNode(int count) {
        super(count);
    }

    public MqttOutNode(String name, int count) {
        super(name, count);
    }

    MqttOutNode(String name) {
        super(name, 1);
    }

    public void connectLocalHost() {
        MqttConnectOptions options;
        try {
            local = new MqttClient("tcp://localhost:1883", UuidCreator.getTimeBased().toString(),
                    null);
            options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(1000);
            options.setWill("test/will", "Disconnected".getBytes(), 2, false);
            local.connect(options);
        } catch (MqttException e) {
            log.error(e.toString());

        }
    }


    @Override
    void preprocess() {
        connectLocalHost();
    }

    @Override
    void process() {

        if (((getInputWire(0) != null) && (getInputWire(0).hasMessage()))) {

            try {
                Message message = getInputWire(0).get();
                JSONObject jsonObject = ((JsonMessage) message).getPayload();

                String topic = jsonObject.getString("topic");
                JSONObject payload = jsonObject.getJSONObject("payload");

                MqttMessage mqttMessage = new MqttMessage();
                mqttMessage.setPayload(payload.toString().getBytes());
                log.info(topic);
                log.info(jsonObject.toString());
                log.info(mqttMessage.toString());
                local.publish(topic, mqttMessage);
            } catch (MqttException e) {
                log.error(e.toString());
            }
        }
    }


}
