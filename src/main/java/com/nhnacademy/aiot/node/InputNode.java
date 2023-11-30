package com.nhnacademy.aiot.node;

import org.json.JSONObject;
import com.nhnacademy.aiot.exception.AlreadyExistsException;
import com.nhnacademy.aiot.exception.InvalidArgumentException;
import com.nhnacademy.aiot.exception.OutOfBoundsException;
import com.nhnacademy.aiot.message.Message;
import com.nhnacademy.aiot.wire.Wire;

public abstract class InputNode extends ActiveNode {
    Wire[] outputWires;

    InputNode(JSONObject json) {
        super(json);
        
    }

    InputNode(String name, int count) {
        super(name);

        if (count <= 0) {
            throw new InvalidArgumentException();
        }

        outputWires = new Wire[count];
    }

    InputNode(int count) {
        super();

        if (count <= 0) {
            throw new InvalidArgumentException();
        }

        outputWires = new Wire[count];
    }

    public void connectOutputWire(int index, Wire wire) {
        if (outputWires.length <= index) {
            throw new OutOfBoundsException();
        }

        if (outputWires[index] != null) {
            throw new AlreadyExistsException();
        }

        outputWires[index] = wire;
    }

    public int getOutputWireCount() {
        return outputWires.length;
    }

    public Wire getoutputWire(int index) {
        if (index < 0 || outputWires.length <= index) {
            throw new OutOfBoundsException();
        }

        return outputWires[index];
    }

    void output(Message message) {
        log.trace("Message Out");
        for (Wire wire : outputWires) {
            if (wire != null) {
                wire.put(message);
            }
        }
    }

}
