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
        String sensor = (String) object.get("sensor");
        String deviceId = (String) object.get("deviceId");
        String place = (String) object.get("place");

        topic = "data/d/" + deviceId + "/e/p/" + place +"/"+ sensor +"/";
        object.put("topic", topic);
        
        sendNode(object);
    }

    void sendNode(JSONObject object) {
        output(new JsonMessage(object));
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
