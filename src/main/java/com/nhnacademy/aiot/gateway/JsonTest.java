package com.nhnacademy.aiot.gateway;

import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.nhnacademy.aiot.node.ActiveNode;
import com.nhnacademy.aiot.node.SplitNode;
import com.nhnacademy.aiot.wire.BufferedWire;
import com.nhnacademy.aiot.wire.Wire;

public class JsonTest {
    public static void main(String[] args) throws Exception {
        HashMap<String, ActiveNode> nodeList = new HashMap<>();
        HashMap<String, List<String>> wireMap = new HashMap<>();
        String[] opt;
        String path = "com.nhnacademy.aiot.node.";
        Object ob = new JSONParser().parse(new FileReader(
                "/home/nhnacademy/mini-project-aiot-gateway1/src/main/java/com/nhnacademy/aiot/setting/nodeSetting.json"));
        JSONArray js = ((JSONArray) ob);
        for (Object node : (JSONArray) js.get(0)) {
            String nodeType = (String) ((JSONObject) node).get("type");
            String nodeId = (String) ((JSONObject) node).get("id");
            Class<?> testClass = Class.forName(path + nodeType);
            Constructor<?> testconstructor = testClass.getConstructor(String.class);
            Object newObj = testconstructor.newInstance(nodeId);
            nodeList.put(nodeId, (ActiveNode) newObj);
            JSONArray wireInfo = (JSONArray) ((JSONObject) node).get("wire");
            if (!wireInfo.isEmpty()) {
                List<String> WireOutList = new ArrayList<>();;
                for (Object w : wireInfo) {
                    ((JSONObject) w).forEach((key, value) -> WireOutList.add((String) value));

                }
                wireMap.put(nodeId, WireOutList);

            }
        }
        for (String input : wireMap.keySet()) {
            for (String w : wireMap.get(input)) {
                Wire wire = new BufferedWire();
                ActiveNode inputNode = nodeList.get(input);
                ActiveNode outputNode = nodeList.get(w);
                Method connectOutputWire =
                        inputNode.getClass().getMethod("connectOutputWire", int.class, Wire.class);
                Method connectInputWire =
                        outputNode.getClass().getMethod("connectInputWire", int.class, Wire.class);

                connectOutputWire.invoke(inputNode, 0, wire);
                connectInputWire.invoke(outputNode, 0, wire);

            }
        }
        for (ActiveNode node : nodeList.values()) {
            if (node instanceof SplitNode) {
                String[] test = {"--an", "application/+/device/+/event/up", "-s", "temperature"};
                ((SplitNode) node).setCommand(test);
            }
            node.start();
        }
        for (Object setting : (JSONArray) js.get(1)) {
            String an = (String) ((JSONObject) setting).get("--an");
            String sensor = (String) ((JSONObject) setting).get("-s");
            System.out.println(an);
            System.out.println(sensor);
            System.out.println();
        }
    }
}
