package com.nhnacademy.aiot.gateway;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.nhnacademy.aiot.node.ActiveNode;
import com.nhnacademy.aiot.node.SplitNode;
import com.nhnacademy.aiot.wire.BufferedWire;
import com.nhnacademy.aiot.wire.Wire;

public class SettingNode {
    protected final static String settingPath =
            "src/main/java/com/nhnacademy/aiot/setting/nodeSetting.json";
    protected final static String path = "com.nhnacademy.aiot.node.";
    private HashMap<String, ActiveNode> nodeList;
    private HashMap<String, List<String>> wireMap;
    private ArrayList<String> splitOption;
    private Object object;

    public SettingNode() {
        this.nodeList = new HashMap<>();
        this.wireMap = new HashMap<>();
        this.splitOption = new ArrayList<>();
        try {
            this.object = new JSONParser().parse(new FileReader(settingPath));
        } catch (IOException | ParseException e) {
        }
    }

    public void makeFlow() {

        try {
            JSONArray flowJson = ((JSONArray) object);
            for (Object node : (JSONArray) flowJson.get(0)) {
                String nodeType = (String) ((JSONObject) node).get("type");
                String nodeId = (String) ((JSONObject) node).get("id");
                Class<?> nodeClass = Class.forName(path + nodeType);
                Constructor<?> nodeConstructor = nodeClass.getConstructor(String.class);
                Object newObj = nodeConstructor.newInstance(nodeId);
                nodeList.put(nodeId, (ActiveNode) newObj);
                JSONArray wireInfo = (JSONArray) ((JSONObject) node).get("wire");
                if (!wireInfo.isEmpty()) {
                    List<String> WireOutList = new ArrayList<>();
                    for (Object w : wireInfo) {
                        ((JSONObject) w).forEach((key, value) -> WireOutList.add((String) value));

                    }
                    wireMap.put(nodeId, WireOutList);

                }
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException
                | IllegalAccessException | InvocationTargetException e) {

        }
    }

    public void connectWire() {
        try {
            for (String input : wireMap.keySet()) {
                for (String w : wireMap.get(input)) {
                    Wire wire = new BufferedWire();
                    ActiveNode inputNode = nodeList.get(input);
                    ActiveNode outputNode = nodeList.get(w);
                    Method connectOutputWire = inputNode.getClass().getMethod("connectOutputWire",
                            int.class, Wire.class);
                    Method connectInputWire = outputNode.getClass().getMethod("connectInputWire",
                            int.class, Wire.class);

                    connectOutputWire.invoke(inputNode, 0, wire);
                    connectInputWire.invoke(outputNode, 0, wire);

                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {

        }
    }

    public void nodeStart() {
        for (ActiveNode node : nodeList.values()) {
            if (node instanceof SplitNode) {
                ((SplitNode) node).setCommand(getArgs());
            }
            node.start();
        }
    }

    private String[] getArgs() {
        JSONArray flowJson = ((JSONArray) object);
        for (Object setting : (JSONArray) flowJson.get(1)) {
            String anValue = (String) ((JSONObject) setting).get("--an");
            splitOption.add("--an");
            splitOption.add(anValue);
            String sensor = (String) ((JSONObject) setting).get("-s");
            splitOption.add("-s");
            splitOption.add(sensor);
        }
        return splitOption.toArray(String[]::new);
    }
}
