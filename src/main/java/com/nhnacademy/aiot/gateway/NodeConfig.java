package com.nhnacademy.aiot.gateway;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.nhnacademy.aiot.node.ActiveNode;
import com.nhnacademy.aiot.node.InputOutputNode;
import com.nhnacademy.aiot.node.SetNode;
import com.nhnacademy.aiot.wire.BufferedWire;
import com.nhnacademy.aiot.wire.Wire;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NodeConfig {


    private ArrayList<JSONObject> classJsonObjects;
    private Map<String, ActiveNode> nodeMap;                 // 해당 노드의 id, 실제 객체
    private Map<String, Map<Integer, List<String>>> wireMap; // < 노드 id(connectOutput), < 몇 번째 출구, 연결할 노드의 id 리스트 > >
    private Map<String, Method> methodMap;                   // wireConnect할 때 사용할 map

    private String applicationName = "#";
    private List<String> sensors;

    private static final String PATH = "com.nhnacademy.aiot.node.";
    private static final String SETTING_PATH = "/home/nhnacademy/Homeworks/mini-project-aiot-gateway-1/src/main/java/com/nhnacademy/aiot/config/setting.json";

    private static int methodFlag = 1;

    private JSONObject jsonParsObj;

    public NodeConfig() {
        classJsonObjects = new ArrayList<>();
        nodeMap = new HashMap<>();
        wireMap = new HashMap<>();
        methodMap = new HashMap<>();
    }

    // node.json에서 node들의 이름 가져와서 classNameList에 저장하기
    public void getClassName() {

        JSONParser parser;

        // JSON 파일 읽기
        try {
            Reader reader = new FileReader(SETTING_PATH);
            parser = new JSONParser(); // Json 데이터 파싱 위한 객체, JSON문자열 또는 파일을 파싱하여 java로 변환

            Object obj = parser.parse(reader); // parser를 사용해 'reader'에서 읽은 JSON파일을 파싱하고, 그 결과를 저장
            jsonParsObj = (JSONObject) obj;

            JSONArray array = new JSONArray();
            array = (JSONArray) jsonParsObj.get("node");

            for (int i = 0; i < array.size(); i++) {
                classJsonObjects.add((JSONObject) array.get(i));
            }

        } catch (FileNotFoundException e) {
            log.info("{} : getClassName()의 FNF", e.getClass());
            e.printStackTrace();
        } catch (IOException e) {
            log.info("{} : getClassName()의 IO", e.getClass());
            e.printStackTrace();
        } catch (ParseException e) {
            log.info("{} : getClassName()의 PE", e.getClass());
            e.printStackTrace();
        }
    }

    // 각 노드에 관련된 wire들의 데이터를 Map에 저장 < node_id : < 출구 번호 : 연결할 노드의 id 리스트 > >
    public void makeWireMap(String nodeId, JSONObject jsonObject) {

        JSONArray wireArray = (JSONArray) jsonObject.get("wires");     
        JSONArray innerArray = new JSONArray();                 // 2차 배열로 감싸여 있어서 한 껍대기 벗긴 json배열 담기 위한 변수
                
        Map<Integer, List<String>> wireInfo = new HashMap<>();  // < 출구 번호 : 연결할 노드의 id 리스트 >
        
        for ( int i = 0; i < wireArray.size(); i++ ) {
            
            List<String> nextNodeName = new ArrayList<>();          // 연결할 노드의 id들 담을 리스트
            innerArray = (JSONArray) wireArray.get(i);

            for ( int j = 0; j < innerArray.size(); j++ ) {
                nextNodeName.add((String) innerArray.get(j));
            }

            wireInfo.put(i, nextNodeName);
            wireMap.put(nodeId, wireInfo);

            //nextNodeName.clear();                             // 왜 애먼 wireMap의 정보들이 삭제되지..?
        }
    }

    // classNameList에 있는 name을 가지고 노드 생성하기, wiremap에 저장
    public void makeInstance() {

        String nodeId;
        String objName;
        int wireCount;

        for (JSONObject obj : classJsonObjects) {
            
            nodeId = (String) obj.get("id");
            objName = (String) obj.get("name");
            wireCount = ((JSONArray)classJsonObjects.get(0).get("wires")).size();   // 각 노드의 wires 출구 갯수 [ [], [] ]면 2

            if ((JSONArray) obj.get("wires") != null) {     // wires가 없는 노드 제외
                makeWireMap(nodeId, obj);                   // wireMap생성
            }

            try {
                Class<?> cls = Class.forName( PATH + objName );  //도메인 클래스명

                Constructor constructor = cls.getConstructor(String.class, int.class);

                ActiveNode activeNode = (ActiveNode) constructor.newInstance(nodeId, wireCount);
                
                nodeMap.put(nodeId, activeNode);

                // wire connect method 까지 미리 저장
                if (methodFlag == 1 && activeNode instanceof InputOutputNode) {
                    Method connectOutputWire = activeNode.getClass().getMethod("connectOutputWire", int.class, Wire.class);
                    Method connectInputWire = activeNode.getClass().getMethod("connectInputWire", int.class, Wire.class);

                    methodMap.put("connectOutputWire", connectOutputWire);
                    methodMap.put("connectInputWire", connectInputWire);

                    methodFlag--;
                }

              } catch (ClassNotFoundException | IllegalArgumentException | SecurityException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                log.info("{} : {} 발생", getClass().getSimpleName(), e.getCause());
                e.printStackTrace();
            }

        }
        //System.out.println(wireMap);                            
    }

    // wire 연결하는 메서드
    void connectWire() {

        Method connectOutputWireMethod = methodMap.get("connectOutputWire");
        Method connectInputWireMethod = methodMap.get("connectInputWire");

        //int nodeIdx = 0;  //나중에 배열안에 연결할 노드가 여러개일 경우 사용할 idx

        for(String nodeIdKey : wireMap.keySet()) {     // 각 노드
            ActiveNode outNode = nodeMap.get(nodeIdKey);   // outNode는 getConnectOutput해야할 노드
            
            Map<Integer, List<String>> wiresInfo = wireMap.get(nodeIdKey);
            
            for(int wireNumber : wiresInfo.keySet()) {
                List<String> nextwireList = new ArrayList<>();
                nextwireList = wiresInfo.get(wireNumber);

                for(String inputNodeId : nextwireList) {
                    //nodeIdx++;
                    ActiveNode inNode = nodeMap.get(inputNodeId);
                    
                    Wire wire = new BufferedWire();
                    try {

                        Method connectOutputWireMethod1 = outNode.getClass().getMethod("connectOutputWire", int.class, Wire.class);
                        Method connectInputWireMethod1 = inNode.getClass().getMethod("connectInputWire", int.class, Wire.class);

                        connectOutputWireMethod1.invoke(outNode, 0, wire);
                        connectInputWireMethod1.invoke(inNode, 0, wire);

                        //connectOutputWireMethod.invoke(outNode, 0, wire);
                        //connectInputWireMethod.invoke(inNode, 0, wire);
                    } catch (IllegalAccessException | IllegalArgumentException
                            | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                        e.printStackTrace();
                    }
                }
                //nodeIdx = 0;
            }
        }
    }

    // commandLine 확인하는 메서드
    void getSettingFromCommandLine(String[] args) {

        Options options = new Options();
        options.addOption(Option.builder("c").desc("c옵션이 있는 경우 CommandLine에서 우선적으로 정보를 가져온다.").build());
        options.addOption(null, "an", true, "application name이 주어질 경우 해당 메시지만 수신하고록 한다.");
        options.addOption(Option.builder("s").hasArg().argName("sensors").desc("장치에 있는 sensor 목록들이다.").build());

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;

        try {
            commandLine = parser.parse(options, args);

            if (commandLine.hasOption("c")) {       // c옵션이 있으면 commandLine 우선
                if (commandLine.hasOption("an")) {
                    applicationName = commandLine.getOptionValue("an");  // application/+/device/+/event/up
                    System.out.println(applicationName);
                }
                if (commandLine.hasOption("s")) {
                    sensors = new ArrayList<>(List.of(commandLine.getOptionValue("s").split(",")));
                    System.out.println(sensors);
                }
            } else {
                getSettingFromSettingJson();
            }

        } catch (org.apache.commons.cli.ParseException e) {
            log.info("{} : setOptions()메서드 {} 발생", getClass().getSimpleName(), e.getClass());
            e.printStackTrace();
        }
    }

    void getSettingFromSettingJson() {

        JSONObject commandObj = (JSONObject) jsonParsObj.get("command");

        for(Object key : commandObj.keySet()) {
            if(key.toString().equals("-s")) {
                List<String> sensorList = new ArrayList<>(List.of(commandObj.get(key).toString().split(",")));
                sensors = sensorList;
            }
            if(key.toString().equals("--an")) {
                applicationName = (String) commandObj.get(key);
            }
        }
    }

    // node start하는 메서드
    void nodeStart() {
        for(ActiveNode node : nodeMap.values()) {
            if(node.getClass().isAssignableFrom(SetNode.class)) {
                SetNode setnode = (SetNode) node;
                setnode.SetApplicationName(applicationName);
                setnode.SetSensors(sensors);
            }
            node.start();
        }
    }
}
