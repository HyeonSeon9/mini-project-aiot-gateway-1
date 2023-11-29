package com.nhnacademy.aiot.node;

import org.json.JSONObject;
import com.nhnacademy.aiot.message.JsonMessage;
import com.nhnacademy.aiot.message.Message;

public class TopicNode extends InputOutputNode {

    private String topic;

    public TopicNode(String name, int inCount, int outCount) {
        super(name, inCount, outCount);
    }

    void makeSendTopic(JSONObject object) {
        String msgSensor = (String) object.get("sensor");
        String deviceId = (String) object.get("deviceId");
        String place = (String) object.get("place");
        System.out.println(msgSensor);
        System.out.println(deviceId);
        System.out.println(place);

        topic = "data/d/" + deviceId + "/e/p/" + place +"/temperature/#";
    }

    @Override
    void preprocess() {
    }

    @Override
    void process() {
        if ((getInputWire(0)!=null) && (getInputWire(0).hasMessage())) {
            Message message = getInputWire(0).get();
            JSONObject object = ((JsonMessage) message).getPayload();
            makeSendTopic(object);
        }
    }

}
