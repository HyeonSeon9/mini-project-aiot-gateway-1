package com.nhnacademy.aiot.node;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import com.nhnacademy.aiot.message.JsonMessage;
import com.nhnacademy.aiot.message.Message;

public class PlaceTranslatorNode extends InputOutputNode {
    HashMap<String, String> placeInfo;
    private String place;

    PlaceTranslatorNode(String name) {
        super(name, 1, 1);
        // TODO Auto-generated constructor stub
    }

    @Override
    void preprocess() {
        placeInfo = new HashMap<>(Map.of("class_a", "강의실 A", "class_b", "강의실 B", "server_room",
                "서버실", "lobby", "로비", "office", "사무실", "storage", "창고", "meeting_room", "미팅룸",
                "pair_room", "페어룸", "냉장고", "냉장고"));

    }

    @Override
    void process() {
        if ((getInputWire(0) != null) && (getInputWire(0).hasMessage())) {
            Message message = getInputWire(0).get();
            JSONObject jsonObject = ((JsonMessage) message).getPayload();
            jsonObject.put("place", placeInfo.get(jsonObject.get("place")));
            System.out.println(jsonObject);
        }
    }


}
