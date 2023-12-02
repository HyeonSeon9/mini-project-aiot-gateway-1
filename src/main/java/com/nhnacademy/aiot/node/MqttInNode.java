package com.nhnacademy.aiot.node;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONObject;
import com.nhnacademy.aiot.message.JsonMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MqttInNode extends InputNode {
    private IMqttClient server = null;

    public MqttInNode() {
        this("mqttInNode", 1);
    }

    public MqttInNode(String name, int cnt) {
        super(name, cnt);
    }

    void connectServer() {
        try {
            server = new MqttClient("tcp://ems.nhnacademy.com", super.getId().toString());

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(1000);
            
            server.connect(options);
        } catch (MqttException e) {
            log.info("{} : connectServer()메서드 {} 발생", getClass().getSimpleName(), e.getClass());
            e.printStackTrace();
        }
    }

    void subscribeServer() {
        try {
            server.subscribe("application/#", (topic, msg) -> { // msg : 24e124fffef79304
                JSONObject object = new JSONObject(msg);                    // msg를 바로 사용하려고 하면 동작 안함 // object : {"retained":true,"qos":0,"payload":[26,16,50,52,101,49,50,52,102,102,102,101,102,55,57,51,49,97],"messageId":0,"id":0,"duplicate":false}
                // System.out.println(new String(msg.getPayload())); // msg에서 payload만 빼와서 byte[]로 만드는건가?
                object.put("topic", topic);
                System.out.println("mqttin");
                if (topic.contains("application")) {
                    JSONObject payload = new JSONObject(new String(msg.getPayload()));
                    if (!payload.getJSONObject("deviceInfo").getString("tenantName").equals("외부 시연") && !payload.getJSONObject("deviceInfo").getString("tenantName").equals("NHN Academy 광주")) {
                        object.put("payload", payload);
                        output(new JsonMessage(object));
                    }
                }
            });
        } catch (MqttException e) {
            log.info("{} : subscribeServer()메서드 {} 발생", getClass().getSimpleName(), e.getClass());
            e.printStackTrace();
        }
    }

    @Override
    void preprocess() {
        connectServer();
        subscribeServer();
    }

    @Override
    void process() {
        if (!server.isConnected()) {
            subscribeServer();
        }
    }

}
