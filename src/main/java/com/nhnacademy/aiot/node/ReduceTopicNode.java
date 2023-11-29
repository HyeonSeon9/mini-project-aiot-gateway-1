package com.nhnacademy.aiot.node;

import org.json.JSONObject;
import com.nhnacademy.aiot.message.JsonMessage;
import com.nhnacademy.aiot.message.Message;

public class ReduceTopicNode extends InputOutputNode {
    private String topic;

    public ReduceTopicNode() {
        super(1, 1);
    }

    public ReduceTopicNode(String name) {
        super(name, 1, 1);
    }


    public String makeTopic(JSONObject jsonObject) {
        String devEui = jsonObject.getString("deviceEui");
        String place = jsonObject.getString("place");
        String sensor = jsonObject.getString("sensor");

        return "data/d/" + devEui + "/p/" + place + "/e/" + sensor;
    }

    // data/d/24e124136d 151547/p/창고/e/temperature:msg.payload:

    @Override
    void preprocess() {}

    @Override
    void process() {

        if (((getInputWire(0) != null) && (getInputWire(0).hasMessage()))) {
            Message message = getInputWire(0).get();
            JSONObject jsonObject = ((JsonMessage) message).getPayload();

            topic = makeTopic(jsonObject);

            jsonObject.put("topic", topic);
            output(new JsonMessage(jsonObject));
        }
    }

    @Override
    synchronized void postprocess() {}


}
