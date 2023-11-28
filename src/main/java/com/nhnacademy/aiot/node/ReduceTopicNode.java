package com.nhnacademy.aiot.node;

import org.json.JSONObject;
import com.nhnacademy.aiot.message.JsonMessage;
import com.nhnacademy.aiot.message.Message;

public class ReduceTopicNode extends InputOutputNode {
    private String topic;

    ReduceTopicNode(String name) {
        super(1, 1);
        // TODO Auto-generated constructor stub
    }


    public String makeTopic(JSONObject jsonObject) {
        String devEui = jsonObject.getString("devEui");
        String place = jsonObject.getString("place");
        String sensor = jsonObject.getString("sensor");
        return "data/d" + devEui + "/p/" + place + "/e/" + sensor;

    }

    @Override
    void preprocess() {

    }

    @Override
    void process() {
        if (getInputWire(0) != null && getInputWire(0).hasMessage()) {
            Message message = getInputWire(0).get();
            JSONObject jsonObject = ((JsonMessage) message).getPayload();

            topic = makeTopic(jsonObject);

            jsonObject.put("topic", topic);
            output(new JsonMessage(jsonObject));
        }
    }



}
