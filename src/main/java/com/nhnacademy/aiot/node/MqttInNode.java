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
            server.subscribe("#", (topic, msg) -> {

                // newMsg.put("deviceId", payload.getJSONObject("deviceInfo").getString("devEui"));
                // newMsg.put("time", payload.getString("time"));
                // newMsg.put("tenant", payload.getJSONObject("deviceInfo").getJSONObject("tags")
                // .getString("branch"));
                // newMsg.put("plcae", payload.getJSONObject("deviceInfo").getJSONObject("tags")
                // .getString("place"));
                // newMsg.put("sensor", payload.getJSONObject("object").getInt("temperature"));

                JSONObject jsonObject = new JSONObject(msg);
                jsonObject.put("topic", topic);
                if (topic.contains("application")) { // byte형식으로 넘어오는 애들 예외 처리
                    JSONObject jsonPayLoad = new JSONObject(new String(msg.getPayload()));
                    jsonObject.put("payload", jsonPayLoad);
                    output(new JsonMessage(jsonObject));
                } else {
                    output(new JsonMessage(jsonObject));
                }
                // payload.getJSONObject("object").keySet().forEach(System.out::println);
            });
        } catch (MqttException e) {

        }
    }

    @Override
    void postprocess() {}

}
