package com.nhnacademy.aiot.gateway;

import java.util.UUID;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        String uuid = UUID.randomUUID().toString();
        try (IMqttClient client = new MqttClient("tcp://ems.nhnacademy.com", uuid)) {

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            options.setWill("test/will", "Disconnected".getBytes(), 1, false);
            options.setKeepAliveInterval(1000);
            client.connect(options);
            // MqttMessage message = new MqttMessage();
            // JSONObject payload = new JSONObject();
            // payload.put("time", new Date().getTime());
            // payload.put("value", 26);
            // message.setPayload(payload.toString().getBytes());
            // client.publish("data/d/24e124128c067999/p/테스트/e/co2", message);
            client.subscribe("application/+/device/+/event/up", (topic, msg) -> {
                // System.out.println("Message received : " + topic + msg);
                JSONObject payload = new JSONObject(new String(msg.getPayload()));
                payload.getJSONObject("object").keySet().forEach(System.out::println);
            });

            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(1000);
            }
            client.setCallback(null);
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
