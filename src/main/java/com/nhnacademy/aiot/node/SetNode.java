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
import org.apache.commons.cli.ParseException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONObject;
import com.nhnacademy.aiot.message.JsonMessage;
import com.nhnacademy.aiot.message.Message;

public class SetNode extends InputOutputNode {

    private String[] args;
    private String an;
    private String applicationName;
    private ArrayList<String> sensors;

    public SetNode(String name, int count) {
        super(name, count,count);
    }

    public SetNode(int count1, int count2) {
        super(count1, count2);
    }

    public void SetApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    void setOptions() {
        
    }

    void setPayload (JSONObject payload) {
                //branch, place, deviceId, time, value

        Set<String> sensorKeySet = payload.getJSONObject("object").keySet();

        sensors = new ArrayList<>(List.of("temperature", "humidity"));              //지우기

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
            
            if (message instanceof JsonMessage && (MqttTopic.isMatched(applicationName, object.getString("topic")))) {
                    setPayload(object.getJSONObject("payload"));
            }
        }
    }

}
