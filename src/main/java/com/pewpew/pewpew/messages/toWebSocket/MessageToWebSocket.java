package com.pewpew.pewpew.messages.toWebSocket;

import com.pewpew.pewpew.messageSystem.Abonent;
import com.pewpew.pewpew.messageSystem.Address;
import com.pewpew.pewpew.messageSystem.Message;
import com.pewpew.pewpew.websoket.GameWebSocket;

public abstract class MessageToWebSocket extends Message {
    public MessageToWebSocket(Address from, Address to) {
        super(from, to);
    }

    @Override
    public final void exec(Abonent abonent) {
        if (abonent instanceof GameWebSocket) {
            exec((GameWebSocket) abonent);
        }
    }

    protected abstract void exec(GameWebSocket service);
}
