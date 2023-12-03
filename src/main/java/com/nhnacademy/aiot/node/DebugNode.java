package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.message.JsonMessage;
import com.nhnacademy.aiot.message.Message;

public class DebugNode extends OutputNode {
    public DebugNode() {
        super(1);
    }

    public DebugNode(String name) {
        super(name, 1);
    }

    public DebugNode(int count) {
        super(count);
    }

    @Override
    void preprocess() {}

    @Override
    void process() {
        for (int i = 0; i < getInputWireCount(); i++) {
            if (getInputWire(i).hasMessage()) {
                log.trace("Message : {}", i);

                Message message = getInputWire(i).get();

                if (message instanceof JsonMessage) {
                    System.out.println(((JsonMessage) message).getPayload());
                }

            }
        }
    }
}
