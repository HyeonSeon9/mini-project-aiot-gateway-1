package com.nhnacademy.aiot.gateway.Node;

import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Hello world!
 *
 */
public class Client {
    public static void main(String[] args) {
        String publisherId = UUID.randomUUID().toString();
        try (IMqttClient client = new MqttClient("tcp://ems.nhnacademy.com", publisherId)) {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            options.setWill("test/will", "Disconnected".getBytes(), 1, true);
            options.setKeepAliveInterval(1000);
            client.connect(options);

            client.publish("test/a/b/c", new MqttMessage("Hello".getBytes()));
            client.subscribe("test/a/b/#", (topic, msg) -> {
                System.out.println(topic + "/" + msg);
            });

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
