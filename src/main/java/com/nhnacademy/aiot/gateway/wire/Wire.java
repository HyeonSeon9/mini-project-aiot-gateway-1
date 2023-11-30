package com.nhnacademy.aiot.gateway.wire;

import com.nhnacademy.aiot.gateway.message.Message;

public interface Wire {

    public void put(Message message);

    public boolean hasMessage();

    public Message get();
}

