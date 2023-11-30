package com.nhnacademy.aiot.gateway.Node;

import com.nhnacademy.aiot.gateway.Exception.AlreadyExistsException;
import com.nhnacademy.aiot.gateway.Exception.InvalidArgumentException;
import com.nhnacademy.aiot.gateway.Exception.OutOfBoundsException;
import com.nhnacademy.aiot.gateway.Wire.Wire;

public abstract class OutputNode extends ActiveNode {
    Wire[] inputWires;

    OutputNode(String name) {
        super(name);
    }

    OutputNode(String name, int count) {
        super(name);

        if (count <= 0) {
            throw new InvalidArgumentException();
        }

        inputWires = new Wire[count];
    }

    OutputNode(int count) {
        super();

        if (count <= 0) {
            throw new InvalidArgumentException();
        }

        inputWires = new Wire[count];
    }

    //

    public void connectInputWire(int index, Wire wire) {
        if (inputWires.length <= index) {
            throw new OutOfBoundsException();
        }

        if (inputWires[index] != null) {
            throw new AlreadyExistsException();
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
}
