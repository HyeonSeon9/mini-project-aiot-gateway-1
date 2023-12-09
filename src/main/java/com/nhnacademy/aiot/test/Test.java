package com.nhnacademy.aiot.test;

import java.util.UUID;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Test {
    // data/d/24e124136d151547/p/창고/e/humidity 
    String publisherId = UUID.randomUUID().toString();

    public void test() {
        try(IMqttClient client = new MqttClient("tcp://ems.nhnacademy.com", publisherId)) {
            client.connect();

            client.subscribe("application/+/device/+/#", (topic, msg) -> {
                // JSONObject payload = new JSONObject(new String(msg.getPayload()));
                // System.out.println("topic : " + topic);
                // System.out.println("message : " + msg);

                String payload = new String(msg.getPayload());
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject message = (JSONObject) parser.parse(payload);

                    JSONObject deviceInfo = (JSONObject) message.get("deviceInfo");
                    JSONObject tags = (JSONObject) deviceInfo.get("tags");
                    JSONObject sensor = (JSONObject) message.get("object");

                    String devEui = (String) deviceInfo.get("devEui");
                    String tenantName = (String) deviceInfo.get("tenantName");
                    String place = (String) tags.get("place");
                    String time = (String) message.get("time");

                    JSONObject newObject = new JSONObject();

                    newObject.put("time", time);
                    newObject.put("tenantName", tenantName);
                    newObject.put("devEui", devEui);
                    newObject.put("place", place);
                    newObject.put("sensor", sensor);

                    System.out.println(newObject.toJSONString());

                } catch(Exception e) {
                    e.printStackTrace();
                }

            });

            while(!Thread.currentThread().isInterrupted()) {
                Thread.sleep(100);
            }

            client.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.test();
    }
}
