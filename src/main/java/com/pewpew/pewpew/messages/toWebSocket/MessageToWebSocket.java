package com.pewpew.pewpew.messages.towebsocket;

import com.pewpew.pewpew.messagesystem.Abonent;
import com.pewpew.pewpew.messagesystem.Address;
import com.pewpew.pewpew.messagesystem.Message;
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
