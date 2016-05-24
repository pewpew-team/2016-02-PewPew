package com.pewpew.pewpew.messages.togamemechanics;

import com.pewpew.pewpew.mechanics.GameMechanicsImpl;
import com.pewpew.pewpew.messagesystem.Address;

public class MessageRegister extends MessageToGameMechanics {
    private final String userName;
    private final Address socketAdress;

    public MessageRegister(Address from, Address to, String userName) {
        super(from, to);
        this.socketAdress = from;
        this.userName = userName;
    }

    @Override
    protected void exec(GameMechanicsImpl gameMechanics) {
        gameMechanics.addUser(userName, socketAdress);
    }
}
