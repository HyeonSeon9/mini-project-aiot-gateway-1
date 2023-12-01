package com.nhnacademy.aiot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.nhnacademy.aiot.node.SplitNode;


class WireCountTest {
    SplitNode splitNode = new SplitNode("SplitNode", 5);

    @Test
    void wireCountTest() {
        int expected = 5;

        Assertions.assertEquals(expected, splitNode.getInputWireCount());
        Assertions.assertEquals(expected, splitNode.getOutputWireCount());
    }
}
