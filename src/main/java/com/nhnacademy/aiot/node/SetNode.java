package com.nhnacademy.aiot.node;

import java.util.ArrayList;
import java.util.Date;
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

    public SetNode(String name, String[] args) {
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
        System.out.println(payload);
        JSONObject newMsg = new JSONObject();
        //branch, place, deviceId, time, value

        Set<String> sensorKeySet = payload.getJSONObject("object").keySet();

        for (String key : sensorKeySet) {
            if (sensors.contains(key))  {
                newMsg.put("time", new Date().getTime());
                newMsg.put("branch",payload.getJSONObject("deviceInfo").getJSONObject("tags").getString("branch"));
                newMsg.put("place",payload.getJSONObject("deviceInfo").getJSONObject("tags").getString("place"));
                newMsg.put("deviceId", payload.getJSONObject("deviceInfo").getString("devEui"));
                newMsg.put("value",payload.getJSONObject("object").get(key));
            }
        }
        System.out.println(newMsg);
        //output(new JsonMessage(newMsg));
    }

    @Override
    void preprocess() {
        setOptions();
    }

    @Override
    void process() {
        if ((getInputWire(0) != null) && (getInputWire(0).hasMessage())) {
            Message message = getInputWire(0).get();
            JSONObject object = ((JsonMessage) message).getPayload();
            if (message instanceof JsonMessage) {
                if(MqttTopic.isMatched(an, object.getString("topic"))) {
                    //System.out.println(object.getString("topic"));
                    setPayload(object.getJSONObject("payload"));
                }
                

                // if(payload.has("deviceInfo")) {
                //     newObj.put("deviceInfo", payload.getJSONObject("deviceInfo"));
                //     newObj.put("tags", payload.getJSONObject("deviceInfo").getJSONObject("tags"));
                //     newObj.put("object", payload.getJSONObject("object"));
                //     output(new JsonMessage(newObj));
                // }
            }
        }
    }

}
