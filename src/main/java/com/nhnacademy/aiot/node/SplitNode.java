package com.nhnacademy.aiot.node;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONObject;

import com.nhnacademy.aiot.message.JsonMessage;
import com.nhnacademy.aiot.message.Message;

public class SplitNode extends InputOutputNode {
    private static final String PAYLOAD = "payload";
    private static final String DEVICE_INFO = "deviceInfo";
    private static final String TENANT_NAME = "tenantName";
    private String aplicationName = "#";
    private List<String> sensors;

    public SplitNode(String name, int count) {
        super(name, 1, count);
    }

    public void setAplicationName(String aplicationName) {
        this.aplicationName = aplicationName;
    }

    public void setSensors(List<String> sensors) {
        this.sensors = sensors;
    }

    void splitSensor(JSONObject jsonObject) {
        JSONObject deviceInfo = jsonObject.getJSONObject(PAYLOAD).getJSONObject(DEVICE_INFO);

        if (deviceInfo.has(TENANT_NAME)
                && deviceInfo.getString(TENANT_NAME).equals("NHN Academy 경남")) {
            Set<String> sensorSet = jsonObject.getJSONObject(PAYLOAD).getJSONObject("object").keySet();
            for (String s : sensorSet) {
                if (sensors.contains(s)) {
                    JSONObject newJson = new JSONObject();
                    JSONObject jsonPayload = new JSONObject();
                    jsonPayload.put("time", new Date().getTime());
                    jsonPayload.put("value",
                            jsonObject.getJSONObject(PAYLOAD).getJSONObject("object").get(s));
                    newJson.put(PAYLOAD, jsonPayload);

                    newJson.put("place", jsonObject.getJSONObject(PAYLOAD)
                            .getJSONObject(DEVICE_INFO).getJSONObject("tags").get("place"));
                    newJson.put("sensor", s);
                    newJson.put("tenant", jsonObject.getJSONObject(PAYLOAD)
                            .getJSONObject(DEVICE_INFO).get(TENANT_NAME));
                    newJson.put("deviceEui", jsonObject.getJSONObject(PAYLOAD)
                            .getJSONObject(DEVICE_INFO).getString("devEui"));

                    newJson.put("prevNode", "SplitNode");
                    sendNode(newJson);
                }
            }
        }
    }

    void sendNode(JSONObject jsonObject) {
        output(new JsonMessage(jsonObject));
    }

    @Override
    void process() {
        if (((getInputWire(0) != null) && (getInputWire(0).hasMessage()))) {
            Message message = getInputWire(0).get();
            JSONObject jsonObject = ((JsonMessage) message).getPayload();
            if (MqttTopic.isMatched(aplicationName, jsonObject.get("topic").toString())) {
                splitSensor(jsonObject);
            }
        }
    }
}
