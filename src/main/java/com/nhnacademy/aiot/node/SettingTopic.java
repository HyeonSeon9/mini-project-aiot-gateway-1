package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.message.Message;
import com.nhnacademy.aiot.message.JsonMessage;
import org.json.JSONObject;



public class SettingTopic extends InputOutputNode {



    public SettingTopic(String name) {
        super(name, 1, 1);
    }

    public SettingTopic(String name, int count) {
        super(name, count, 1);
    }

    public String makeTopic(JSONObject jsonObject) {
        String stringTopic = "topic";
        String deviceId = jsonObject.getString("deviceEui");
        String place = jsonObject.getString("place");
        String sensor = jsonObject.getString("sensor");
        String topic = "data/d/" + deviceId + "/p/" + place + "/e/" + sensor;
        JSONObject json = new JSONObject();
        json.put(stringTopic, topic);

        return json.getString(stringTopic);
    }



    @Override
    void process() {
        String topic;
        if (getInputWire(0) != null && getInputWire(0).hasMessage()) {
            Message message = getInputWire(0).get();
            JSONObject jsonObject = ((JsonMessage) message).getPayload();

            topic = makeTopic(jsonObject);
            jsonObject.put("topic", topic);
            output(new JsonMessage(jsonObject));
        }
    }

}
