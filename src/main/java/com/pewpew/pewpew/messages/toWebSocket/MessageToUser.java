package com.pewpew.pewpew.messages.toWebSocket;

import com.pewpew.pewpew.messageSystem.Address;
import com.pewpew.pewpew.websoket.GameWebSocket;

public class MessageToUser extends MessageToWebSocket {
    private final String gameFrame;

    public MessageToUser(Address from, Address to, String gameFrame) {
        super(from, to);
        this.gameFrame = gameFrame;
    }

    @Override
    protected void exec(GameWebSocket service) {
        service.sendMessage(gameFrame);
    }
}
