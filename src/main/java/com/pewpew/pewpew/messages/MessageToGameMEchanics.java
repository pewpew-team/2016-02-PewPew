package com.pewpew.pewpew.messages;

import com.pewpew.pewpew.mechanics.GameMechanics;
import com.pewpew.pewpew.mechanics.GameMechanicsImpl;
import com.pewpew.pewpew.messageSystem.Abonent;
import com.pewpew.pewpew.messageSystem.Address;
import com.pewpew.pewpew.messageSystem.Message;

public abstract class MessageToGameMechanics extends Message {
    public MessageToGameMechanics(Address from, Address to) {
        super(from, to);
    }

    @Override
    public final void exec(Abonent abonent) {
        if (abonent instanceof GameMechanics) {
            exec((GameMechanicsImpl) abonent);
        }
    }

    protected abstract void exec(GameMechanicsImpl service);

}
