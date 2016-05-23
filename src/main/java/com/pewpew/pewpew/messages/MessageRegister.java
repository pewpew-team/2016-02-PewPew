package com.pewpew.pewpew.messages;

import com.pewpew.pewpew.mechanics.GameMechanicsImpl;
import com.pewpew.pewpew.messageSystem.Address;

public class MessageRegister extends MessageToGameMechanics {
    private String userName;
    private Address socketAdress;

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
