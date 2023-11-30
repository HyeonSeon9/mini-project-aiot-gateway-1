package com.nhnacademy.aiot.gateway.Wire;

import com.nhnacademy.aiot.gateway.Message.Message;

public interface Wire {
    public void put(Message message);

    public boolean hasMessage();

    public Message get();
}

