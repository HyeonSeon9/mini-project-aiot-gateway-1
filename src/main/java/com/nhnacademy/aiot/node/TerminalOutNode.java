package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.message.JsonMessage;
import com.nhnacademy.aiot.message.Message;

public class TerminalOutNode extends OutputNode {
    public TerminalOutNode() {
        super(1);
    }

    public TerminalOutNode(String name) {
        super(name, 1);
    }

    public TerminalOutNode(int count) {
        super(count);
    }

    @Override
    void preprocess() {
        setInterval(1);
    }

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
