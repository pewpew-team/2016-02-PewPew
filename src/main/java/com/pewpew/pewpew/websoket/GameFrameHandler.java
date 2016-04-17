package com.pewpew.pewpew.websoket;

import com.pewpew.pewpew.mechanics.GameMechanics;
import com.pewpew.pewpew.model.GameChanges;
import org.jetbrains.annotations.NotNull;


public class GameFrameHandler extends MessageHandler<GameChanges> {

    private WebSocketService webSocketService;
    private GameMechanics gameMechanics;

    public GameFrameHandler(WebSocketService webSocketService,
                            GameMechanics gameMechanics) {
        super(GameChanges.class);
        this.webSocketService = webSocketService;
        this.gameMechanics = gameMechanics;
    }

    @Override
    public void handle(@NotNull GameChanges gameChanges, @NotNull String userName) throws HandleException {
        gameMechanics.changeState(gameChanges, userName);
    }
}
