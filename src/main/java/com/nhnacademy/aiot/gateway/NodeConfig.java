package com.nhnacademy.aiot.gateway;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.nhnacademy.aiot.node.ActiveNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NodeConfig {


    private ArrayList<JSONObject> classJsonObjects;
    private Map<String, ActiveNode> nodeMap; // 해당 노드의 id, 실제 객체
    private Map<String, Map<Integer, List<String>>> wireMap; // < 해당 노드의 id, < 몇 번째 출구, 연결할 노드의 id 리스트 > >


    private String applicationName = "#";
    private ArrayList<String> sensors;

    private boolean commandLine;

    private static final String PATH = "com.nhnacademy.aiot.node.";


    public NodeConfig() {
        classJsonObjects = new ArrayList<>();
        nodeMap = new HashMap<>();
        wireMap = new HashMap<>();
    }

    // node.json에서 node들의 이름 가져와서 classNameList에 저장하기
    public void getClassName() {
        JSONParser parser;

        // JSON 파일 읽기
        try {
            Reader reader = new FileReader(
                    "/home/nhnacademy/Homeworks/mini-project-aiot-gateway-1/src/main/java/com/nhnacademy/aiot/config/setting.json");
            parser = new JSONParser(); // Json 데이터 파싱 위한 객체, JSON문자열 또는 파일을 파싱하여 java로 변환

            Object obj = parser.parse(reader); // parser를 사용해 'reader'에서 읽은 JSON파일을 파싱하고, 그 결과를 저장
            JSONObject jsObj = (JSONObject) obj;

            JSONArray array = new JSONArray();
            array = (JSONArray) jsObj.get("node");

            for (int i = 0; i < array.size(); i++) {
                classJsonObjects.add((JSONObject) array.get(i));
            }

            System.out.println(classJsonObjects);

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
        JSONArray wireArray = (JSONArray) jsonObject.get("wires");     // 각 노드의 wires 출구 갯수 [ [], [] ]면 2
        System.out.println(nodeId);
        System.out.println(wireArray);
        System.out.println("-----------------");
        JSONArray innerArray = new JSONArray();
                
        Map<Integer, List<String>> wireInfo = new HashMap<>();
        List<String> nextNodeName = new ArrayList<>();

        for ( int i = 0; i < wireArray.size(); i++ ) {
            innerArray = (JSONArray) wireArray.get(i);
            
            for ( int j = 0; j < innerArray.size(); j++ ) {
                nextNodeName.add((String) innerArray.get(j));
            }

            wireInfo.put(i, nextNodeName);
            wireMap.put(nodeId, wireInfo);          //왜 여기서...
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
            wireCount = ((JSONArray)classJsonObjects.get(0).get("wires")).size();

            if ((JSONArray) obj.get("wires") != null) {     // wires가 없는 노드 제외
                makeWireMap(nodeId, obj);                           // wireMap생성
            }

            try {
                Class<?> cls = Class.forName( PATH + objName );  //도메인 클래스명

                //어차피 생성자 여러개니깐 일단 id랑 wirecount 두 개로 만들어보아요
                Constructor constructor = cls.getConstructor(String.class, int.class);

                ActiveNode activeNode = (ActiveNode) constructor.newInstance(nodeId, wireCount);
                
                nodeMap.put(nodeId, activeNode);

              } catch (ClassNotFoundException | IllegalArgumentException | SecurityException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                log.info("{} : {} 발생", getClass().getSimpleName(), e.getCause());
                e.printStackTrace();
            }
            
        }
    }

    // commandLine 확인하는 메서드

    // 생성하는 메서드

    // wire 연결하는 메서드


    // node start하는 메서드
}
