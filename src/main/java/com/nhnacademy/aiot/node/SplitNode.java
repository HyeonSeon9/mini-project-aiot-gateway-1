package com.nhnacademy.aiot.node;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONObject;
import com.nhnacademy.aiot.message.JsonMessage;
import com.nhnacademy.aiot.message.Message;

public class SplitNode extends InputOutputNode {
    private String aplicationName = "#";
    private ArrayList<String> sensors;

    public SplitNode(String name, int count) {
        super(name, count, count);
    }

    public void setAplicationName(String aplicationName) {
        this.aplicationName = aplicationName;
    }

    public void setSensors(ArrayList<String> sensors) {
        this.sensors = sensors;
    }

    void splitSensor(JSONObject jsonObject) {
        if (jsonObject.getJSONObject("payload").getJSONObject("deviceInfo").getString("tenantName")
                .equals("NHN Academy 경남")) {
            Set<String> sensorSet =
                    jsonObject.getJSONObject("payload").getJSONObject("object").keySet();
            for (String s : sensorSet) {
                if (sensors.contains(s)) {
                    JSONObject newJson = new JSONObject();
                    JSONObject payload = new JSONObject();
                    payload.put("time", new Date().getTime());
                    payload.put("value",
                            jsonObject.getJSONObject("payload").getJSONObject("object").get(s));
                    newJson.put("payload", payload);

                    newJson.put("place", jsonObject.getJSONObject("payload")
                            .getJSONObject("deviceInfo").getJSONObject("tags").get("place"));
                    newJson.put("sensor", s);
                    newJson.put("tenant", jsonObject.getJSONObject("payload")
                            .getJSONObject("deviceInfo").get("tenantName"));
                    newJson.put("deviceEui", jsonObject.getJSONObject("payload")
                            .getJSONObject("deviceInfo").getString("devEui"));
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
