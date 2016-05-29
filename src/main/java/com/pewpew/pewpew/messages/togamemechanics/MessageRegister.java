package com.pewpew.pewpew.messages.togamemechanics;

import com.pewpew.pewpew.mechanics.GameMechanicsImpl;
import com.pewpew.pewpew.messagesystem.Address;
import com.pewpew.pewpew.websoket.GameWebSocket;

public class MessageRegister extends MessageToGameMechanics {
    private final Address socketAdress;
    private final String userName;

    public MessageRegister(Address from, Address to, String userName ) {
        super(from, to);
        this.socketAdress = from;
        this.userName = userName;
    }

    @Override
    protected void exec(GameMechanicsImpl gameMechanics) {
        gameMechanics.addUser(userName, socketAdress);
    }
}
