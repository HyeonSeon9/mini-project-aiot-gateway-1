package com.nhnacademy.aiot.node;

import org.json.JSONObject;
import com.nhnacademy.aiot.message.JsonMessage;
import com.nhnacademy.aiot.message.Message;

public class ReduceTopicNode extends InputOutputNode {
    private String topic;

    public ReduceTopicNode(String name, int count) {
        super(name, count, count);
    }


    public String makeTopic(JSONObject jsonObject) {
        StringBuilder stringBuilder = new StringBuilder();
        String devEui = jsonObject.getString("deviceEui");
        String place = jsonObject.getString("place");
        String sensor = jsonObject.getString("sensor");

        stringBuilder.append("data/d/");
        stringBuilder.append(devEui);
        stringBuilder.append("/p/");
        stringBuilder.append(place);
        stringBuilder.append("/e/");
        stringBuilder.append(sensor);


        return stringBuilder.toString();
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