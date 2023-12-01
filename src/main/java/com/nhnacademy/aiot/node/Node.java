package com.nhnacademy.aiot.node;

import java.util.UUID;
import com.github.f4b6a3.uuid.UuidCreator;
import org.json.JSONObject;

public abstract class Node {
    UUID id;
    String name;
    private static int count;

    Node(JSONObject json) {
        if (json.has("id")) {
            id = UuidCreator.fromString((String) json.get("id"));
        } else {
            id = UuidCreator.getTimeBased();
        }
    }

    Node() {
        this("Node" + count, UuidCreator.getTimeBased());
    }

    Node(String name, UUID id) {
        count++;
        this.id = id;
        this.name = name;
    }

    Node(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

    }

    public JSONObject getJson() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("name", name);

        return object;
    }
}
