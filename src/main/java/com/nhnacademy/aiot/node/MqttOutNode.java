package com.nhnacademy.aiot.node;

import java.io.UnsupportedEncodingException;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
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
            try {
                String topic = object.getString("topic");
                JSONObject payload = new JSONObject(object.getString("payload"));
                byte[] payloadBytes = payload.toString().getBytes("UTF-8");

                MqttMessage msg = new MqttMessage();
                System.out.println("+++++" + topic);
                msg.setPayload(payloadBytes);
                System.out.println("-----" + msg);
                msg.setQos(0);
                msg.setRetained(true);
                //toTelegraf.publish(topic, payload);
            } catch (JSONException | UnsupportedEncodingException e) { 
                // TODO: handle exception
            }
        }
    }

}
