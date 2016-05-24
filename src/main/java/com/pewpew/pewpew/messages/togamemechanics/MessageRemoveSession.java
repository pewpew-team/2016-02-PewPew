package com.pewpew.pewpew.messages.togamemechanics;

import com.pewpew.pewpew.mechanics.GameMechanicsImpl;
import com.pewpew.pewpew.messagesystem.Address;

public class MessageRemoveSession extends MessageToGameMechanics {
    private final String userName;

    public MessageRemoveSession(Address from, Address to, String userName) {
        super(from, to);
        this.userName = userName;
    }

    @Override
    protected void exec(GameMechanicsImpl service) {
        service.closeGameSession(userName);
    }
}
