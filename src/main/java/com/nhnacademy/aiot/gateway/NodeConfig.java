package com.nhnacademy.aiot.gateway;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.nhnacademy.aiot.node.Node;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NodeConfig {
    
    private ArrayList<JSONObject> classNameObjects;
    private ArrayList<String> classNameList;
    private ArrayList<Node> nodeList;

    public NodeConfig() {
        classNameObjects = new ArrayList<>();
        classNameList = new ArrayList<>();
        nodeList = new ArrayList<>();
    }

    //node.json에서 node들의 이름 가져와서 classNameList에 저장하기
    public void getClassName() {
        JSONParser parser;

        //JSON 파일 읽기
        try {
            Reader reader = new FileReader("/home/nhnacademy/Homeworks/mini-project-aiot-gateway-1/src/main/java/com/nhnacademy/aiot/config/node.json");
            parser = new JSONParser(); //Json 데이터 파싱 위한 객체, JSON문자열 또는 파일을 파싱하여 java로 변환
            
            Object obj  = parser.parse(reader);     // parser를 사용해 'reader'에서 읽은 JSON파일을 파싱하고, 그 결과를 저장
            JSONObject jsObj = (JSONObject) obj;
            
            JSONArray array = new JSONArray();
            array = (JSONArray) jsObj.get("node");

            System.out.println(array.get(0));
            
            //오브젝트에서 이름, wire의 사이즈, 노드의 id?를 가지고 객체와 wire를 할 수가 있나?
            // 생성하는 메서드
            // wire 연결하는 메서드


            // classNameObjects.addAll(array);

            // for ( int i = 0; i < array.size(); i++ ) {
            //     classNameList.add((String) classNameObjects.get(i).get("name"));
            // }

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

    // classNameList에 있는 name을 가지고 노드 생성하기
    public void makeInstance() {

        for (String name : classNameList) {
            try {
                Class<?> cls = Class.forName(name);
                //Constructor constructor = cls.getConstructor(String.class, int, ); //만약 노드마다 생성자 파라미터가 다르면..? -> 일단 노드별 기본 생성자가 wire 1개씩 만들어지게 만들고, 질문으로 wire의 갯수를 지정해주려면 어케하는지 질문해보자
            } catch (ClassNotFoundException | IllegalArgumentException | SecurityException e) {
                log.info("{} : {} 발생", getClass().getSimpleName(), e.getCause());
                e.printStackTrace();
            }
        }

    }
}