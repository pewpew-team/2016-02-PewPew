package com.pewpew.pewpew.messages.togamemechanics;

import com.pewpew.pewpew.mechanics.GameMechanicsImpl;
import com.pewpew.pewpew.messagesystem.Abonent;
import com.pewpew.pewpew.messagesystem.Address;
import com.pewpew.pewpew.messagesystem.Message;

public abstract class MessageToGameMechanics extends Message {
    public MessageToGameMechanics(Address from, Address to) {
        super(from, to);
    }

    @Override
    public final void exec(Abonent abonent) {
        if (abonent instanceof GameMechanicsImpl) {
            exec((GameMechanicsImpl) abonent);
        }
    }

    protected abstract void exec(GameMechanicsImpl service);

}
