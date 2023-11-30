package com.nhnacademy.aiot.gateway.node;

import com.nhnacademy.aiot.gateway.exception.OutOfBoundsException;
import com.nhnacademy.aiot.gateway.message.Message;
import com.nhnacademy.aiot.gateway.wire.Wire;

public abstract class InputOutputNode extends ActiveNode {
    Wire[] inputWires;
    Wire[] outputWires;

    InputOutputNode(String name, int inCount, int outCount) {
        super(name);

        inputWires = new Wire[inCount];
        outputWires = new Wire[outCount];
    }

    InputOutputNode(int inCount, int outCount) {
        super();

        inputWires = new Wire[inCount];
        outputWires = new Wire[outCount];
    }

    public void connectOutputWire(int index, Wire wire) {
        if (index < 0 || outputWires.length <= index) {
            throw new OutOfBoundsException();
        }

        outputWires[index] = wire;
    }

    public int getOutputWireCount() {
        return outputWires.length;
    }

    public Wire getOutputWire(int index) {
        if (index < 0 || outputWires.length <= index) {
            throw new OutOfBoundsException();
        }

        return outputWires[index];
    }

    public void connectInputWire(int index, Wire wire) {
        if (index < 0 || inputWires.length <= index) {
            throw new OutOfBoundsException();
        }

        inputWires[index] = wire;
    }

    public int getInputWireCount() {
        return inputWires.length;
    }

    public Wire getInputWire(int index) {
        if (index < 0 || inputWires.length <= index) {
            throw new OutOfBoundsException();
        }

        return inputWires[index];
    }

    void output(Message message) {
        for (Wire port : outputWires) {
            if (port != null) {
                port.put(message);
            }
        }
    }
}
