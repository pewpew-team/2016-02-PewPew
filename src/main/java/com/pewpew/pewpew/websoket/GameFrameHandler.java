package com.pewpew.pewpew.websoket;

import com.google.gson.Gson;
import com.pewpew.pewpew.mechanics.GameMechanics;
import com.pewpew.pewpew.model.BulletObject;
import com.pewpew.pewpew.model.GameFrame;
import com.pewpew.pewpew.model.PlayerObject;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class GameFrameHandler extends MessageHandler<GameFrame> {

    private WebSocketService webSocketService;
    private GameMechanics gameMechanics;

    public GameFrameHandler(WebSocketService webSocketService,
                            GameMechanics gameMechanics) {
        super(GameFrame.class);
        this.webSocketService = webSocketService;
        this.gameMechanics = gameMechanics;
    }

    @Override
    public void handle(@NotNull GameFrame message, @NotNull String userName) throws HandleException {
        final Gson gson = new Gson();
        PlayerObject enemy = message.getPlayer();
        message.setPlayer(message.getEnemy());
        message.setEnemy(enemy);
        BulletObject newBullet = message.getBullets().get(0);
        message.setBullets(gameMechanics.bulletsCalculation(newBullet, userName));
        String json = gson.toJson(message);
        try {
            webSocketService.sendMessageToUser(json, userName);
        } catch (IOException e) {
            try {
                webSocketService.notifyGameOver(userName, true);
            } catch (IOException en) {
                throw new HandleException("Unnable to send answer back to user " + userName, en);
            }
        }
    }
}
