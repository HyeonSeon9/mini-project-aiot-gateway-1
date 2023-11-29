package com.nhnacademy.aiot.gateway;

import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.nhnacademy.aiot.node.ActiveNode;
import com.nhnacademy.aiot.node.InputNode;

public class JsonTest {
    public static void main(String[] args) throws Exception {
        HashMap<String, ActiveNode> nodeList = new HashMap<>();

        String path = "com.nhnacademy.aiot.node.";
        Object ob = new JSONParser().parse(new FileReader(
                "/home/nhnacademy/mini-project-aiot-gateway1/src/main/java/com/nhnacademy/aiot/setting/nodeSetting.json"));
        JSONArray js = ((JSONArray) ob);
        // System.out.println(js);

        for (Object node : (JSONArray) js.get(0)) {
            String nodeType = (String) ((JSONObject) node).get("type");
            String nodeId = (String) ((JSONObject) node).get("id");
            Class<?> testClass = Class.forName(path + nodeType);
            Constructor<?> testconstructor = testClass.getConstructor(String.class);
            Object newObj = testconstructor.newInstance(nodeId);
            nodeList.put(nodeId, (ActiveNode) newObj);
            JSONArray wire = (JSONArray) ((JSONObject) node).get("wire");
            if (!wire.isEmpty()) {
                for (Object w : wire) {
                    ((JSONObject) w)
                            .forEach((key, value) -> System.out.println(nodeId + " : " + value));
                }

            }
        }

        for (ActiveNode node : nodeList.values()) {
            if (node instanceof InputNode) {
                System.out.println("hihi");
            }
        }
        for (Object setting : (JSONArray) js.get(1)) {
            String an = (String) ((JSONObject) setting).get("--an");
            String sensor = (String) ((JSONObject) setting).get("-s");
            System.out.println(an);
            System.out.println(sensor);
        }
    }
}
