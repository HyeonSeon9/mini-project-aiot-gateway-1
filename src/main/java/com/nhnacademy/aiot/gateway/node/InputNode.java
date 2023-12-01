package com.nhnacademy.aiot.gateway.node;

import org.json.simple.JSONObject;
import com.nhnacademy.aiot.gateway.wire.Wire;
import com.nhnacademy.aiot.gateway.exception.AlreadyExistsException;
import com.nhnacademy.aiot.gateway.exception.InvalidArgumentException;
import com.nhnacademy.aiot.gateway.exception.OutOfBoundsException;
import com.nhnacademy.aiot.gateway.message.Message;

public abstract class InputNode extends ActiveNode {
    Wire[] outputWires;

    InputNode(String name) {
        this(name, 1);
    }

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
        for (Wire wire : outputWires) {
            if (wire != null) {
                wire.put(message);
            }
        }
    }

}

