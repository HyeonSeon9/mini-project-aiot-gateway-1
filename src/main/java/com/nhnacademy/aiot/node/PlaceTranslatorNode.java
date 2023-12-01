package com.nhnacademy.aiot.node;

import java.util.HashMap;
import org.json.JSONObject;
import com.nhnacademy.aiot.message.JsonMessage;
import com.nhnacademy.aiot.message.Message;

public class PlaceTranslatorNode extends InputOutputNode {
    HashMap<String, String> placeInfo;

    public PlaceTranslatorNode(String name, int count) {
        super(name, 1, 1);
    }

    public void setPlaceInfo(HashMap<String, String> placeInfo) {
        this.placeInfo = placeInfo;
    }

    @Override
    void process() {
        if (((getInputWire(0) != null) && (getInputWire(0).hasMessage()))) {
            Message message = getInputWire(0).get();
            JSONObject jsonObject = ((JsonMessage) message).getPayload();
            jsonObject.put("place", placeInfo.get(jsonObject.get("place")));
            output(new JsonMessage(jsonObject));
        }
    }
}
