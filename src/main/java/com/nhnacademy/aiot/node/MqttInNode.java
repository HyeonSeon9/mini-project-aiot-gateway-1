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
                server.subscribe("application/#", (topic, msg) -> {         // msg : 24e124fffef79304
                    JSONObject object = new JSONObject(msg);   //바로 msg sout하면 안나옴  // object : {"retained":true,"qos":0,"payload":[26,16,50,52,101,49,50,52,102,102,102,101,102,55,57,51,49,97],"messageId":0,"id":0,"duplicate":false}
                    //System.out.println(new String(msg.getPayload()));       // msg에서 payload만 빼와서 byte[]로 만드는건가?
                    object.put("topic", topic);

                    if (topic.contains("application")) {
                        JSONObject payload = new JSONObject(new String(msg.getPayload()));
                        //System.out.println(payload);
                        if (payload.get("object") != null && payload.get("deviceInfo") != null) {
                            object.put("payload", payload);
                            output(new JsonMessage(object));
                        }
                    }
                    
                    // payload.getJSONObject("object").keySet().forEach(System.out::println);
                });
        } catch (MqttException e) {

        }
    }

    @Override
    void postprocess() {}

}
