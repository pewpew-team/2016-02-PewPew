package com.pewpew.pewpew.websoket;

import com.google.gson.Gson;
import com.pewpew.pewpew.model.GameFrame;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class GameFrameHandler extends MessageHandler<GameFrame> {

    private WebSocketService webSocketService;
   public GameFrameHandler(WebSocketService webSocketService) {
       super(GameFrame.class);
       this.webSocketService = webSocketService;
    }
    @Override
    public void handle(@NotNull GameFrame message, @NotNull String userName) throws HandleException {
        final Gson gson = new Gson();
        String json = gson.toJson(message);
        try {
            webSocketService.sendMessageToUser(json, userName);
        } catch (IOException e) {
            throw new HandleException("Unnable to send answer back to user "+ userName, e);
        }
    }
}
