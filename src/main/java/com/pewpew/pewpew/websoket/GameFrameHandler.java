package com.pewpew.pewpew.websoket;

import com.pewpew.pewpew.mechanics.GameMechanics;
import com.pewpew.pewpew.model.GameChanges;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


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
        //FIXME: It s some dangerous multithread
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(()->gameMechanics.changeState(gameChanges, userName));
    }
}
