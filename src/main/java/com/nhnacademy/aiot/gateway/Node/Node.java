package com.nhnacademy.aiot.gateway.Node;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Node {
    static int count;
    String id;

    Node() {
        count++;
        id = String.format("%s-%02d", getClass().getSimpleName(), count);
        log.trace("create node : {}", id);
    }

    public String getId() {
        return id;
    }

    public abstract String getName();

    public abstract void setName(String name);

    public static int getCount() {
        return count;
    }
}
