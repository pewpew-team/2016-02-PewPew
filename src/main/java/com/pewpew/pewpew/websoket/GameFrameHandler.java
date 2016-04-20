package com.pewpew.pewpew.websoket;

import com.pewpew.pewpew.mechanics.GameMechanics;
import com.pewpew.pewpew.model.GameChanges;
import org.jetbrains.annotations.NotNull;


public class GameFrameHandler extends MessageHandler<GameChanges> {

    private final GameMechanics gameMechanics;

    public GameFrameHandler(GameMechanics gameMechanics) {
        super(GameChanges.class);
        this.gameMechanics = gameMechanics;
    }

    @Override
    public void handle(@NotNull GameChanges gameChanges, @NotNull String userName) {
        gameMechanics.changeState(gameChanges, userName);
    }
}
