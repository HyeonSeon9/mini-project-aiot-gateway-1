package com.nhnacademy.aiot.node;

import org.json.JSONObject;
import com.nhnacademy.aiot.message.JsonMessage;
import com.nhnacademy.aiot.message.Message;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DebugNode extends OutputNode {

    public DebugNode(String name, int count) {
        super(name, count);
    }

    @Override
    void preprocess() {
        log.info("Node Start");
    }

    @Override
    void process() {

        while (getInputWire(0).hasMessage()) {
            Message message = getInputWire(0).get();
            JSONObject jsonObject = ((JsonMessage) message).getPayload();
            log.info("{}", jsonObject);
        }
    }
}
