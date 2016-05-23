package com.pewpew.pewpew.mechanics;

import com.pewpew.pewpew.messageSystem.Abonent;
import com.pewpew.pewpew.messageSystem.Address;
import com.pewpew.pewpew.messageSystem.Message;
import com.pewpew.pewpew.model.GameFrame;
import com.pewpew.pewpew.websoket.WebSocketService;

public abstract class GameStateMessage extends Message {

    private GameFrame gameFrame;
    public GameStateMessage(Address from, Address to, GameFrame gameFrame) {
        super(from, to);
        this.gameFrame = gameFrame;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof WebSocketService) {
            exec((WebSocketService) abonent);
        }
    }
    protected void exec(WebSocketService webSocketService) {

    }


//    protected abstract void exec(WebSocketService webSocketService)
}
