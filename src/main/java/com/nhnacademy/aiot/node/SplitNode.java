package com.nhnacademy.aiot.node;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONObject;
import com.nhnacademy.aiot.message.JsonMessage;
import com.nhnacademy.aiot.message.Message;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SplitNode extends InputOutputNode {
    private String aplicationName = "#";

    private ArrayList<String> sensors;
    private String[] args;
    private static final String DEVICEINFO = "deviceInfo";
    private static final String PAYLOAD = "payload";

    public SplitNode(String name) {
        super(name, 1, 1);
    }

    public void setCommand(String[] args) {
        this.args = args;
    }

    public void setOptions() {
        Options commandOptions;
        commandOptions = new Options();
        commandOptions.addOption(null, "an", true, "application name이 주어질 경우 해당 메시지만 수신하도록 한다.");
        commandOptions.addOption("s", null, true, "Test");
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;
        try {
            commandLine = parser.parse(commandOptions, args);

            if (commandLine.hasOption("an")) {
                this.aplicationName = commandLine.getOptionValue("an");
            }
            if (commandLine.hasOption("s")) {
                sensors = new ArrayList<>(List.of(commandLine.getOptionValue("s").split(",")));
            }
        } catch (ParseException e) {
            log.error(e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("test", commandOptions);
        }
    }

    void splitSensor(JSONObject jsonObject) {
        Set<String> sensorSet = jsonObject.getJSONObject(PAYLOAD).getJSONObject("object").keySet();

        if (jsonObject.getJSONObject(PAYLOAD).getJSONObject(DEVICEINFO).get("tenantName")
                .equals("NHN Academy 경남")) {

            for (String s : sensorSet) {
                if (sensors.contains(s)) {
                    JSONObject newJson = new JSONObject();
                    JSONObject payload = new JSONObject();
                    payload.put("time", new Date().getTime());
                    payload.put("value",
                            jsonObject.getJSONObject(PAYLOAD).getJSONObject("object").get(s));
                    newJson.put(PAYLOAD, payload);

                    newJson.put("place", jsonObject.getJSONObject(PAYLOAD).getJSONObject(DEVICEINFO)
                            .getJSONObject("tags").get("place"));
                    newJson.put("sensor", s);
                    newJson.put("tenant", jsonObject.getJSONObject(PAYLOAD)
                            .getJSONObject(DEVICEINFO).get("tenantName"));
                    newJson.put("deviceEui", jsonObject.getJSONObject(PAYLOAD)
                            .getJSONObject(DEVICEINFO).getString("devEui"));
                    log.info(newJson.toString());
                    sendNode(newJson);
                }
            }
        }

    }

    void sendNode(JSONObject jsonObject) {
        output(new JsonMessage(jsonObject));
    }

    @Override
    void preprocess() {
        setOptions();
    }

    @Override
    void process() {
        if (((getInputWire(0) != null) && (getInputWire(0).hasMessage()))) {
            Message message = getInputWire(0).get();
            JSONObject jsonObject = ((JsonMessage) message).getPayload();
            if (MqttTopic.isMatched(aplicationName, jsonObject.get("topic").toString())) {
                splitSensor(jsonObject);
            }
        }
    }
}
