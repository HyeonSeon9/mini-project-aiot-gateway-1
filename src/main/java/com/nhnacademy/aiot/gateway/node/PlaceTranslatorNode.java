package com.nhnacademy.aiot.gateway.node;

import java.util.Map;
import org.json.JSONObject;
import com.nhnacademy.aiot.gateway.message.JsonMessage;
import com.nhnacademy.aiot.gateway.message.Message;

public class PlaceTranslatorNode extends InputOutputNode {
    Map<String, String> placeInfo;

    public PlaceTranslatorNode(String name, int count) {
        super(name, count, count);
    }

    public void setPlaceInfo(Map<String, String> placeInfo) {
        this.placeInfo = placeInfo;
    }

    @Override
    void process() {
        if ((getInputWire(0) != null) && (getInputWire(0).hasMessage())) {
            Message message = getInputWire(0).get();
            JSONObject jsonObject = ((JsonMessage)message).getPayload();
            jsonObject.put("place", placeInfo.get(jsonObject.get("place")));
            output(new JsonMessage(jsonObject));
        }
    }
}
