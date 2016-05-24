package com.pewpew.pewpew.messages.towebsocket;

import com.pewpew.pewpew.messagesystem.Address;
import com.pewpew.pewpew.websoket.GameWebSocket;

public class MessageGameStart extends MessageToWebSocket {
    private final String userName;

    public MessageGameStart(Address from, Address to, String userName) {
        super(from, to);
        this.userName = userName;
    }

    @Override
    protected void exec(GameWebSocket service) {
        System.out.println("Got message startGame: " + userName);
        service.startGame();
        service.setEnemyName(userName);
    }
}
