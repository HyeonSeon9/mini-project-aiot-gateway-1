package com.nhnacademy.aiot.gateway;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import lombok.extern.slf4j.Slf4j;
import com.nhnacademy.aiot.node.ActiveNode;


@Slf4j
public class Test {
    public static void main(String[] args) {
        HashMap<String, ActiveNode> nodeMap = new HashMap<>();
        HashMap<String, List<String>> wireMap = new HashMap<>();

        try {
            Reader reader =
                    new FileReader("src/main/java/com/nhnacademy/aiot/setting/nodeSetting.json");
            JSONParser parser = new JSONParser();
            String path = "com.nhnacademy.aiot.node.";
            Object object;
            object = parser.parse(reader);
            JSONArray jsonArray = (JSONArray) object;

            for (Object o : (JSONArray) jsonArray.get(0)) {
                String id = (String) ((JSONObject) o).get("id");
                String type = (String) ((JSONObject) o).get("type");
                Class<?> nodeClass = Class.forName(path + type);
                Constructor<?> constructor =
                        nodeClass.getDeclaredConstructor(String.class, int.class);
                Object obj = constructor.newInstance(id, 1);
                nodeMap.put(type, (ActiveNode) obj);

                JSONArray wireInfo = (JSONArray) ((JSONObject) o).get("wire");
                List<String> wireOut = new ArrayList<>();
                for (Object w : wireInfo) {
                    Iterable<?> iter = ((JSONObject) w).values();
                    iter.forEach(value -> wireOut.add((String) (value)));
                }
                wireMap.put(id, wireOut);
            }



        } catch (IOException | ParseException | ClassNotFoundException | NoSuchMethodException
                | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

}
