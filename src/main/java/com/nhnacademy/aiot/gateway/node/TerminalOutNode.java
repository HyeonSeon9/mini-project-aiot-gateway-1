package com.nhnacademy.aiot.gateway.node;

import com.nhnacademy.aiot.gateway.message.JsonMessage;
import com.nhnacademy.aiot.gateway.message.Message;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
                    log.debug("Message : {}", ((JsonMessage) message).getPayload());
                }
            }
        }
    }
}
