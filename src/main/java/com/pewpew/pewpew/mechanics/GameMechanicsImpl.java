package com.pewpew.pewpew.mechanics;

import com.google.gson.Gson;
import com.pewpew.pewpew.model.BulletObject;
import com.pewpew.pewpew.model.GameChanges;
import com.pewpew.pewpew.model.GameFrame;
import com.pewpew.pewpew.model.PlayerObject;
import com.pewpew.pewpew.websoket.WebSocketService;
import com.pewpew.pewpew.websoket.WebSocketServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
public class GameMechanicsImpl implements GameMechanics {

    private static final Double Y_MAX = 720.0;
    private static final Double X_MAX = 1280.0;

    @NotNull
    private WebSocketService webSocketService;

    @NotNull
    private Set<GameSession> allSessions = new HashSet<>();

    @NotNull
    private Map<String, GameSession> nameToGame = new HashMap<>();

    @Nullable
    private volatile String waiter;

    @NotNull
    private final Gson gson;

    @NotNull
    private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();

    static ScheduledExecutorService timer =
            Executors.newSingleThreadScheduledExecutor();

    public GameMechanicsImpl(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
        this.gson = new Gson();
    }

    public void addUser(@NotNull String user) {
        addUserInternal(user);
    }

    public String getEnemy(@NotNull String user) {
        if (nameToGame.get(user).getPlayerTwo() != user) {
            return nameToGame.get(user).getPlayerTwo();
        }
        return nameToGame.get(user).getPlayerOne();
    }

    private void addUserInternal(@NotNull String user) {
        if (waiter != null) {
            //noinspection ConstantConditions
            starGame(user, waiter);
            waiter = null;
        } else {
            waiter = user;
        }
    }

    private void starGame(@NotNull String first, @NotNull String second) {
        GameSession gameSession = new GameSession(first, second);
        allSessions.add(gameSession);

        nameToGame.put(first, gameSession);
        nameToGame.put(second, gameSession);

        webSocketService.notifyStartGame(gameSession.getPlayerOne());
        webSocketService.notifyStartGame(gameSession.getPlayerTwo());
        timer.scheduleAtFixedRate(
                () -> sendState(gameSession),0,30, TimeUnit.MILLISECONDS);
    }


    public void sendState(GameSession gameSession) {
        GameFrame gameFrame = gameSession.getGameFrame();
        gameFrame.moveBullets();
        try {
            String gameFrameJson = gson.toJson(gameFrame);
            webSocketService.sendMessageToUser(gameFrameJson, gameSession.getPlayerOne());
        } catch (IOException e) {
            e.printStackTrace();
            allSessions.remove(gameSession);
            nameToGame.remove(gameSession.getPlayerOne());
            nameToGame.remove(gameSession.getPlayerTwo());
        }
        try {

            PlayerObject player = gameFrame.getEnemy();
            gameFrame.setEnemy(gameFrame.getPlayer());
            gameFrame.setPlayer(player);
            gameFrame.translateToAnotherCoordinateSystem(X_MAX, Y_MAX);
            String gameFrameJson = gson.toJson(gameFrame);
            gameFrame.translateToAnotherCoordinateSystem(X_MAX, Y_MAX);
            webSocketService.sendMessageToUser(gameFrameJson, gameSession.getPlayerTwo());

        } catch (IOException e) {
            e.printStackTrace();
            allSessions.remove(gameSession);
            nameToGame.remove(gameSession.getPlayerOne());
            nameToGame.remove(gameSession.getPlayerTwo());
        }
    }

    @Override
    public void changeState(GameChanges gameChanges, String userName) {
        GameSession gameSession = nameToGame.get(userName);
        if (gameSession.getPlayerOne().equals(userName)) {
            BulletObject bulletObject = gameChanges.getBullet();
            bulletObject.translateToAnotherCoordinateSystem(X_MAX, Y_MAX);
            gameChanges.setBullet(bulletObject);
        }
        gameSession.changeState(gameChanges);
    }

    @Override
    public void addNewBullet(BulletObject bullet, String user) {
        final GameSession gameSession = nameToGame.get(user);
        if (user.equals(gameSession.getPlayerOne())) {
            bullet.translateToAnotherCoordinateSystem(X_MAX, Y_MAX);
        }
        gameSession.setBulletObject(bullet);
    }

    public ArrayList<BulletObject> bulletsCalculation(String user) {
        GameSession gameSession = nameToGame.get(user);
        return gameSession.getAllBullets();
    }
}
