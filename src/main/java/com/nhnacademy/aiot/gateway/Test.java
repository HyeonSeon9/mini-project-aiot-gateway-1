package com.nhnacademy.aiot.gateway;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Test {
    public static void main(String[] args) {

        try {
            Reader reader = new FileReader(
                    "/home/dongmin/Desktop/java/Week14/mini-project-aiot-gateway-1/.vscode/Transfrom.json");
            JSONParser parser = new JSONParser();
            JSONObject object;
            object = (JSONObject) parser.parse(reader);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

}
