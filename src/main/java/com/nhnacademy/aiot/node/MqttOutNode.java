package com.nhnacademy.aiot.node;

import java.io.UnsupportedEncodingException;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.json.JSONException;
import org.json.JSONObject;
import com.nhnacademy.aiot.message.JsonMessage;
import com.nhnacademy.aiot.message.Message;

public class MqttOutNode extends OutputNode {
    
    private IMqttClient toTelegraf;
    private MqttConnectOptions options;

    public MqttOutNode(String name) {
        super(name, 1);
    }

    @Override
    void preprocess() {
        try{
            toTelegraf = new MqttClient("tcp://localhost", super.getId().toString());
            options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            options.setWill("test/will", "Disconnected".getBytes(), 1, true);
            //options.setKeepAliveInterval(1000);
            toTelegraf.connect();
        } catch (Exception e) {
            
        }
    }

    @Override
    void process() {
        if((getInputWire(0) != null) && getInputWire(0).hasMessage()) {
            Message message = getInputWire(0).get();
            JSONObject object = ((JsonMessage)message).getPayload();
            System.out.println(object);
            try {
                String topic = object.getString("topic");
                JSONObject payload = object.getJSONObject("payload");       // new JSONObject로 한 번더 감싸지마 바보야
                System.out.println(payload);
                
                byte[] payloadBytes = payload.toString().getBytes("UTF-8");

                MqttMessage msg = new MqttMessage();
                msg.setPayload(payloadBytes);
                msg.setQos(2);
                msg.setRetained(true);
                toTelegraf.publish(topic, msg);
                //toTelegraf.publish(topic, payload.toString().getBytes("UTF-8"), 2, false);
            } catch (JSONException | UnsupportedEncodingException | MqttException e) {
                // TODO: handle exception
            }
        }
    }
}