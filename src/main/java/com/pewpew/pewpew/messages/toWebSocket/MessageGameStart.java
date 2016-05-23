package com.pewpew.pewpew.messages.toWebSocket;

import com.pewpew.pewpew.messageSystem.Address;
import com.pewpew.pewpew.websoket.GameWebSocket;

public class MessageGameStart extends MessageToWebSocket {
    private final String userName;

    public MessageGameStart(Address from, Address to, String userName) {
        super(from, to);
        this.userName = userName;
    }

    @Override
    protected void exec(GameWebSocket service) {
        service.sendMessage(userName);
    }
}
