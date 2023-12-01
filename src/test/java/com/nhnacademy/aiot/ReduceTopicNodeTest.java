package com.nhnacademy.aiot;

import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.nhnacademy.aiot.node.ReduceTopicNode;

class ReduceTopicNodeTest {

    @Test
    void makeTopicTest() {
        ReduceTopicNode reduceTopicNode = new ReduceTopicNode("name", 1);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deviceEui", "4e124136d151547");
        jsonObject.put("place", "창고");
        jsonObject.put("sensor", "temperature");

        String deviceEui = jsonObject.getString("deviceEui");
        String place = jsonObject.getString("place");
        String sensor = jsonObject.getString("sensor");

        String expectedTopic = "data/d/" + deviceEui + "/p/" + place + "/e/" + sensor;

        Assertions.assertEquals(expectedTopic ,reduceTopicNode.makeTopic(jsonObject));
    }
}
