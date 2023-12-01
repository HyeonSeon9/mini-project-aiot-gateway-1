package com.nhnacademy.aiot.gateway.node;

import java.util.Date;
import java.util.List;
import java.util.Set;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONObject;
import com.nhnacademy.aiot.gateway.exception.NoSuchDevInfoException;
import com.nhnacademy.aiot.gateway.exception.NoSuchJsonObjectException;
import com.nhnacademy.aiot.gateway.exception.NoSuchPayloadException;
import com.nhnacademy.aiot.gateway.message.JsonMessage;
import com.nhnacademy.aiot.gateway.message.Message;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SplitNode extends InputOutputNode {
    private static final String PAYLOAD = "payload";
    private static final String DEVICE_INFO = "deviceInfo";
    private static final String TENANT_NAME = "tenantName";
    private String applicationName = "#";
    private List<String> sensors;
    
    public SplitNode(String name, int count) {
        super(name, count, count);
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public void setSensors(List<String> sensors) {
        this.sensors = sensors;
    }

    public void splitSensor(JSONObject jsonObject) {

        try {
            JSONObject payloadJsonObject = jsonObject.getJSONObject(PAYLOAD);
            JSONObject deviceInfoJsonObject = payloadJsonObject.getJSONObject(DEVICE_INFO);

            if (deviceInfoJsonObject.has(TENANT_NAME) && deviceInfoJsonObject.getString(TENANT_NAME).equals("NHN Academy 경남")) {
                Set<String> sensorSet = jsonObject.getJSONObject(PAYLOAD).getJSONObject("object").keySet();
                for (String s : sensorSet) {
                    if (sensors.contains(s)) {
                        JSONObject newJson = new JSONObject();
                        JSONObject payload = new JSONObject();
                        payload.put("time", new Date().getTime());
                        payload.put("value",
                        jsonObject.getJSONObject(PAYLOAD).getJSONObject("object").get(s));
                        newJson.put(PAYLOAD, payload);
    
                        newJson.put("place", jsonObject.getJSONObject(PAYLOAD).getJSONObject(DEVICE_INFO).getJSONObject("tags").get("place"));
                        newJson.put("sensor", s);
                        newJson.put("tenant", jsonObject.getJSONObject(PAYLOAD).getJSONObject(DEVICE_INFO).get(TENANT_NAME));
                        newJson.put("deviceEui", jsonObject.getJSONObject(PAYLOAD).getJSONObject(DEVICE_INFO).getString("devEui"));
                        sendNode(newJson);
                    }
                }
            }
        } catch (NoSuchJsonObjectException | NoSuchPayloadException | NoSuchDevInfoException e) {
            log.error("Exception 발생 : {}", e);
        }
    }

    void sendNode(JSONObject object) {
        output(new JsonMessage(object));
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
