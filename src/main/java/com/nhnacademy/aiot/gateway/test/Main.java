package com.nhnacademy.aiot.gateway.test;

import java.util.UUID;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Main {
    public static void main(String[] args) {
        String publisherId = UUID.randomUUID().toString();
        try(IMqttClient client = new MqttClient("tcp://ems.nhnacademy.com", publisherId)) {
            MqttConnectOptions options = new MqttConnectOptions();

            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);

            client.connect();

            // client.publish("test/a/b/c", new MqttMessage("Hello".getBytes()));
            client.subscribe("application/+/device/+/#", (topic, msg) -> {
                // System.out.println("Message received: " + topic);
                System.out.println(msg);
            });

            while(!Thread.currentThread().isInterrupted()) {
                Thread.sleep(100);
            }

            client.disconnect();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
