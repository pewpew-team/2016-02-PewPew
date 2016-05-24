package com.pewpew.pewpew.messages.togamemechanics;

import com.pewpew.pewpew.mechanics.GameMechanicsImpl;
import com.pewpew.pewpew.messagesystem.Address;

public class MessagePauseGame extends MessageToGameMechanics {
    private final String userName;

    public MessagePauseGame(Address from, Address to, String userName) {
        super(from, to);
        this.userName = userName;
    }

    @Override
    protected void exec(GameMechanicsImpl service) {
        service.pauseGame(userName);
    }
}
