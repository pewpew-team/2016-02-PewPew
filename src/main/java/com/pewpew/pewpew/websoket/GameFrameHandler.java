package com.pewpew.pewpew.websoket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pewpew.pewpew.model.GameFrame;
import org.jetbrains.annotations.NotNull;

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
    }
}
