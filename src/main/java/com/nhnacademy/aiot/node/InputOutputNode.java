package com.nhnacademy.aiot.node;

import java.util.ArrayList;
import java.util.List;

import com.nhnacademy.aiot.exception.OutOfBoundsException;
import com.nhnacademy.aiot.message.Message;
import com.nhnacademy.aiot.wire.Wire;

public abstract class InputOutputNode extends ActiveNode {
    Wire[] inputWires;
    List<ArrayList<Wire>> outputWires;

    InputOutputNode(String name, int inCount, int outCount) {
        super(name);

        inputWires = new Wire[inCount];
        outputWires = new ArrayList<>();
        for (int i = 0; i < outCount; i++) {
            ArrayList<Wire> wireList = new ArrayList<>();
            outputWires.add(wireList);
        }
    }

    InputOutputNode(int inCount, int outCount) {
        super();

        inputWires = new Wire[inCount];
        outputWires = new ArrayList<>();
        for (int i = 0; i < outCount; i++) {
            ArrayList<Wire> wireList = new ArrayList<>();
            outputWires.add(wireList);
        }
    }

    public void connectOutputWire(int index, Wire wire) {
        if (index < 0 || outputWires.size() <= index) {
            throw new OutOfBoundsException();
        }
        outputWires.get(index).add(wire);
    }

    public int getOutputWireCount() {
        return outputWires.size();
    }

    public List<Wire> getoutputWire(int port) {
        if (port < 0 || outputWires.size() <= port) {
            throw new OutOfBoundsException();
        }

        return outputWires.get(port);
    }

    public Wire getoutputWire(int port, int index) {
        if (index < 0 || outputWires.size() <= index) {
            throw new OutOfBoundsException();
        }

        return outputWires.get(port).get(index);
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
        for (ArrayList<Wire> wireList : outputWires) {
            for (Wire wire : wireList) {
                wire.put(message);
            }
        }
    }
}
