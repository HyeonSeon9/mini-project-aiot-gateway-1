package com.nhnacademy.aiot.node;

import org.json.JSONObject;
import com.nhnacademy.aiot.message.JsonMessage;
import com.nhnacademy.aiot.message.Message;

public class ConcatNode extends InputOutputNode {
    
    public ConcatNode(String name) {
        this(name, 1, 1);
    }

    public ConcatNode(String name, int count) {
        super(name, count, count);
    }

    public ConcatNode(String name, int inputcount, int outputcount) {
        super(name, inputcount, outputcount);
    }

    void concatAll(JSONObject object) {
        JSONObject jsObject = new JSONObject();
        jsObject.put("topic", object.get("topic"));
        jsObject.put("payload", object.get("payload"));

        sendNode(jsObject);
    }

    void sendNode(JSONObject object) {
        output(new JsonMessage(object));
    }

    @Override
    void process() {
        if ((getInputWire(0) != null) && getInputWire(0).hasMessage()) {
            Message message = getInputWire(0).get();
            JSONObject object = ((JsonMessage) message).getPayload();
            
            concatAll(object);
        }   
    }
}
