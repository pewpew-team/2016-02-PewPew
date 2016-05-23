package com.pewpew.pewpew.messages.toWebSocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pewpew.pewpew.messageSystem.Address;
import com.pewpew.pewpew.websoket.GameWebSocket;

public class MessageGameOver extends MessageToWebSocket {
    private final Boolean won;

    public MessageGameOver(Address from, Address to, Boolean won) {
        super(from, to);
        this.won = won;
    }

    @Override
    protected void exec(GameWebSocket service) {
        service.setGameEnded(true);
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("win", won);
        final Gson gson = new Gson();
        service.sendMessage(gson.toJson(jsonObject));
    }
}
