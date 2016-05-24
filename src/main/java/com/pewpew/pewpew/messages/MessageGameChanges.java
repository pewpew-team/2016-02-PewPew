package com.pewpew.pewpew.messages;

import com.pewpew.pewpew.mechanics.GameMechanicsImpl;
import com.pewpew.pewpew.messageSystem.Address;
import com.pewpew.pewpew.model.GameChanges;

public class MessageGameChanges extends MessageToGameMechanics {
    private GameChanges gameChanges;
    private String userName;

    public MessageGameChanges(Address from, Address to, GameChanges gameChanges, String userName) {
        super(from, to);
        this.gameChanges = gameChanges;
        this.userName = userName;
    }

    @Override
    protected void exec(GameMechanicsImpl gameMechanics) {
        System.out.println("got message - changeGame and sending information to enemy: " + userName);
        gameMechanics.changeState(gameChanges, userName);
    }
}
