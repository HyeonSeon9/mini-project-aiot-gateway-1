package com.nhnacademy.aiot.node;

import java.util.ArrayList;
import java.util.List;

import com.nhnacademy.aiot.exception.InvalidArgumentException;
import com.nhnacademy.aiot.exception.OutOfBoundsException;
import com.nhnacademy.aiot.message.Message;
import com.nhnacademy.aiot.wire.Wire;

public abstract class InputNode extends ActiveNode {
    List<ArrayList<Wire>> outputWires;

    InputNode(String name) {
        this(name, 1);
    }

    InputNode(String name, int count) {
        super(name);

        if (count <= 0) {
            throw new InvalidArgumentException();
        }
        outputWires = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ArrayList<Wire> wireList = new ArrayList<>();
            outputWires.add(wireList);
        }
    }

    InputNode(int count) {
        super();

        if (count <= 0) {
            throw new InvalidArgumentException();
        }

        this.outputWires = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ArrayList<Wire> wireList = new ArrayList<>();
            outputWires.add(wireList);
        }
    }

    public void connectOutputWire(int index, Wire wire) {
        if (outputWires.size() <= index) {
            throw new OutOfBoundsException();
        }
        this.outputWires.get(index).add(wire);
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

    void output(Message message) {
        for (ArrayList<Wire> wireList : outputWires) {
            for (Wire wire : wireList) {
                wire.put(message);
            }
        }
        // for (Wire wire : outputWires) {
        // if (wire != null) {
        // wire.put(message);
        // }
        // }
    }

}
