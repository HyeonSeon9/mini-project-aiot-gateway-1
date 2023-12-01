package com.nhnacademy.aiot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.nhnacademy.aiot.node.TerminalOutNode;

class TerminalOutNodeTest {
    TerminalOutNode terminalOutNode;

    @Test
    void constructorTest() {
        terminalOutNode = new TerminalOutNode("TerminalOutNode");
        String expected = "TerminalOutNode";

        Assertions.assertEquals(expected, terminalOutNode.getName());
    }

    @Test
    void wireCountTest() {
        terminalOutNode = new TerminalOutNode(5);
        int expected = 5;

        Assertions.assertEquals(expected, terminalOutNode.getInputWireCount());
    }
}
