package com.nhnacademy.aiot.gateway.node;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONObject;
import com.nhnacademy.aiot.gateway.message.JsonMessage;
import com.nhnacademy.aiot.gateway.message.Message;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SplitNode extends InputOutputNode {
    private String applicationName = "#";
    private String[] args;
    private ArrayList<String> sensors;
    
    public SplitNode(String name) {
        super(name, 1, 1);
    }

    public void setCommand(String[] args) {
        this.args = args;
    }
    
    public void setOptions() {
        Options commandOptions;
        commandOptions = new Options();
        commandOptions.addOption("--an", "Application Name", true, "프로그램 옵션으로 Application Name을 줄 수 있다");
        commandOptions.addOption("-s", "Sensor Type", true, "허용 가능한 센서 종류를 지정할 수 있다.");
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;

        try {
            commandLine = parser.parse(commandOptions, args);

            if (commandLine.hasOption("--an")) {
                this.applicationName = commandLine.getOptionValue("--an");
            }

            if (commandLine.hasOption("-s")) {
                sensors = new ArrayList<>(List.of(commandLine.getOptionValue("-s").split(",")));
            }
        } catch (ParseException e) {
            log.error("{}", e);
        }
    }

    public void splitSensor(JSONObject object) {
        if (object.getJSONObject("payload").getJSONObject("deviceInfo").getString("tenantName").equals("NHN Academy 경남")) {
            Set<String> sensorSet = object.getJSONObject("payload").getJSONObject("object").keySet();
            for (String s : sensorSet) {
                JSONObject newObject = new JSONObject();
                JSONObject payload = new JSONObject();

                payload.put("time", new Date().getTime());
                payload.put("value", object.getJSONObject("payload").getJSONObject("object").get(s));

                newObject.put("payload", payload);
                newObject.put("place", object.getJSONObject("place").getJSONObject("deviceInfo").getJSONObject("tags").get("place"));
                newObject.put("sensor", s);
                newObject.put("tenantName", object.getJSONObject("payload").getJSONObject("deviceInfo").get("tenantName"));
                newObject.put("deviceEui", object.getJSONObject("payload").getJSONObject("deviceInfo").get("devEui"));

                sendNode(object);
            }
        }
    }

    void sendNode(JSONObject object) {
        output(new JsonMessage(object));
    }

    @Override
    void preprocess() {
        setOptions();
    }

    @Override
    void process() {
        if ((getInputWire(0) != null) && (getInputWire(0).hasMessage())) {
            Message message = getInputWire(0).get();
            JSONObject object = ((JsonMessage)message).getPayload();
            if (MqttTopic.isMatched(applicationName, object.get("topic").toString())) {
                splitSensor(object);
            }
        }
    }
}
