package com.nhnacademy.aiot.gateway;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.nhnacademy.aiot.node.ActiveNode;
import com.nhnacademy.aiot.node.PlaceTranslatorNode;
import com.nhnacademy.aiot.node.SplitNode;
import com.nhnacademy.aiot.wire.BufferedWire;
import com.nhnacademy.aiot.wire.Wire;

public class SettingNode {
    protected static String settingPath =
            "src/main/java/com/nhnacademy/aiot/setting/nodeSetting.json";
    protected static String path = "com.nhnacademy.aiot.node.";

    private HashMap<String, ActiveNode> nodeList;
    private HashMap<String, List<String>> wireMap;

    private ArrayList<String> splitOption;
    private Object object;

    private String aplicationName = "#";
    private ArrayList<String> sensors;

    private boolean commandLine;

    public SettingNode() {
        this.nodeList = new HashMap<>();
        this.wireMap = new HashMap<>();
        this.splitOption = new ArrayList<>();
        try {
            this.object = new JSONParser().parse(new FileReader(settingPath));
        } catch (IOException | ParseException e) {
        }
    }

    public void commandLineOn() {
        this.commandLine = true;
    }

    public boolean isCommandLine() {
        return this.commandLine;
    }

    public void setAplicationName(String aplicationName) {
        this.aplicationName = aplicationName;
    }

    public void setSensors(ArrayList<String> sensors) {
        this.sensors = sensors;
    }

    public void makeFlow() {

        try {
            JSONArray flowJson = ((JSONArray) object);
            for (Object node : (JSONArray) flowJson.get(0)) {
                String nodeType = (String) ((JSONObject) node).get("type");
                String nodeId = (String) ((JSONObject) node).get("id");
                Class<?> nodeClass = Class.forName(path + nodeType);
                Constructor<?> nodeConstructor = nodeClass.getConstructor(String.class, int.class);
                Object newObj = nodeConstructor.newInstance(nodeId, 1);
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
                splitNodeSetup(((SplitNode) node));
            } else if (node instanceof PlaceTranslatorNode) {
                ((PlaceTranslatorNode) node).setPlaceInfo(new HashMap<>(Map.of("class_a", "강의실 A",
                        "class_b", "강의실 B", "server_room", "서버실", "lobby", "로비", "office", "사무실",
                        "storage", "창고", "meeting_room", "미팅룸", "pair_room", "페어룸", "냉장고", "냉장고")));
            }
            node.start();
        }
    }

    public void splitNodeSetup(SplitNode node) {
        if (isCommandLine()) {
            node.setAplicationName(aplicationName);
            node.setSensors(sensors);
            return;
        }
        setSplitOption(node);
    }

    private void setSplitOption(SplitNode node) {
        JSONArray flowJson = ((JSONArray) object);
        for (Object setting : (JSONArray) flowJson.get(1)) {
            this.aplicationName = (String) ((JSONObject) setting).get("--an");

            this.sensors = new ArrayList<>(
                    List.of(((String) ((JSONObject) setting).get("-s")).split(",")));
            node.setAplicationName(aplicationName);
            node.setSensors(sensors);
        }
    }
}