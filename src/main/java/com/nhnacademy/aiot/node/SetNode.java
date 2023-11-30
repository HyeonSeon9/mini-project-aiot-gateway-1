package com.nhnacademy.aiot.node;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONObject;
import com.nhnacademy.aiot.message.JsonMessage;
import com.nhnacademy.aiot.message.Message;

public class SetNode extends InputOutputNode {

    private String[] args;
    private String an;
    private ArrayList<String> sensors;

    public SetNode(String name, int inCount, int outCount, String[] args) {
        super(name, 1,1);
        this.args = args;
    }

    public SetNode(int count1, int count2) {
        super(count1, count2);
    }

    void setOptions() {
        Options options = new Options();
        options.addOption(null, "an", true, "application name이 주어질 경우 해당 메시지만 수신하고록 한다.");
        options.addOption(Option.builder("s").hasArg().argName("sensors").desc("장치에 있는 sensor").build());

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;
        try {
            commandLine = parser.parse(options, args);
            
            if (commandLine.hasOption("an")) {
                an = commandLine.getOptionValue("an");  //application/+/device/+/event/up
            }
            if (commandLine.hasOption("s")) {
                sensors = new ArrayList<>(List.of(commandLine.getOptionValue("s").split(",")));
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    void setPayload (JSONObject payload) {
                //branch, place, deviceId, time, value

        Set<String> sensorKeySet = payload.getJSONObject("object").keySet();
        
        for (String key : sensorKeySet) {
            if (sensors.contains(key)) {
                JSONObject newMsg = new JSONObject();       //for문 바깥에 하면 왜 안됨? 덮어씌우기가 안되나?
                JSONObject newPayload = new JSONObject();
                newPayload.put("time", new Date().getTime());
                newPayload.put("value",payload.getJSONObject("object").get(key));
                newMsg.put("payload", newPayload);
                payload.getJSONObject("deviceInfo").getJSONObject("tags").getString("branch");
                newMsg.put("branch",payload.getJSONObject("deviceInfo").getJSONObject("tags").getString("branch"));
                newMsg.put("place",payload.getJSONObject("deviceInfo").getJSONObject("tags").getString("place"));
                newMsg.put("deviceId", payload.getJSONObject("deviceInfo").getString("devEui"));
                newMsg.put("sensor",key);
                output(new JsonMessage(newMsg));
            }
        }
    }

    @Override
    void preprocess() {
        setOptions();
    }

    @Override
    void process() {
        if ((getInputWire(0) != null) && (getInputWire(0).hasMessage())) {
            Message message = getInputWire(0).get();
            JSONObject object = ((JsonMessage) message).getPayload();   //JsonMessage의 getPayload()
            if (message instanceof JsonMessage) {
                if(MqttTopic.isMatched(an, object.getString("topic"))) {
                    setPayload(object.getJSONObject("payload"));
                }
            }
        }
    }

}
