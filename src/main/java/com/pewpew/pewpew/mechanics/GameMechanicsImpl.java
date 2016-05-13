package com.pewpew.pewpew.mechanics;

import com.google.gson.Gson;
import com.pewpew.pewpew.common.TimeHelper;
import com.pewpew.pewpew.model.Bullet;
import com.pewpew.pewpew.model.GameChanges;
import com.pewpew.pewpew.model.GameFrame;
import com.pewpew.pewpew.model.PlayerObject;
import com.pewpew.pewpew.websoket.WebSocketService;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Clock;
import java.util.*;
import java.util.concurrent.*;

public class GameMechanicsImpl implements GameMechanics {

    private final Double yMax;
    private final Double xMax;

    private final long stepTime;

    @NotNull
    private final WebSocketService webSocketService;

    @NotNull
    private final Set<GameSession> allSessions = new HashSet<>();

    @NotNull
    private final Map<String, GameSession> nameToGame = new HashMap<>();

    @Nullable
    private volatile String waiter;

    @NotNull
    private final Gson gson;

    @NotNull
    private final Clock clock = Clock.systemDefaultZone();

    @NotNull
    private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();

    public GameMechanicsImpl(@NotNull WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
        this.gson = new Gson();

        final Properties property = new Properties();
        final String path = new File("").getAbsolutePath() + "/resources/game.properties";
        try(FileInputStream fileInputStream =
                    new FileInputStream(path)) {
            property.load(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Can't handle game resourses");
        }
        stepTime = Integer.valueOf(property.getProperty("game.stepTime"));
        xMax = Double.valueOf(property.getProperty("map.xMax"));
        yMax = Double.valueOf(property.getProperty("map.yMax"));
    }

    @Override
    public void addUser(@NotNull String user) {
        tasks.add(() -> addUserInternal(user));
    }

    @Override
    public String getEnemy(@NotNull String user) {
        if (!Objects.equals(nameToGame.get(user).getPlayerTwo(), user)) {
            return nameToGame.get(user).getPlayerTwo();
        }
        return nameToGame.get(user).getPlayerOne();
    }

    private void addUserInternal(@NotNull String user) {
        if (waiter != null) {
            //noinspection ConstantConditions
            startGame(user, waiter);
            waiter = null;
        } else {
            waiter = user;
        }
    }

    public void removeSession(Session session) {
        allSessions.remove(session);
    }

    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        long lastFrameMilles = stepTime;
        while (true) {
            final long before = clock.millis();
            gameStep(lastFrameMilles);
            final long after = clock.millis();
            if (after - before > stepTime) {
                System.out.println("gm is lagging. step is " + (after - before) + "ms");
            }
            TimeHelper.sleep(stepTime - (after - before));
            final long afterSleep = clock.millis();
            lastFrameMilles = afterSleep - before;
        }
    }

    private void gameStep(long timeTick) {
        while (!tasks.isEmpty()) {
            final Runnable nextTask = tasks.poll();
            if (nextTask != null) {
                try {
                    nextTask.run();
                } catch (RuntimeException ex) {
                    System.out.println("Cant handle game task " + ex);
                }
            }
        }
        for (GameSession session : allSessions) {
            sendState(session, timeTick);
            if (session.getPlayerOneWon() != null) {
                final Boolean firstWin = session.getPlayerOneWon();
                webSocketService.notifyGameOver(session.getPlayerTwo(), firstWin);
                webSocketService.notifyGameOver(session.getPlayerOne(), !firstWin);
            }
        }
    }

    private void startGame(@NotNull String first, @NotNull String second) {
        final GameSession gameSession = new GameSession(first, second, xMax, yMax);
        allSessions.add(gameSession);

        nameToGame.put(first, gameSession);
        nameToGame.put(second, gameSession);

        webSocketService.notifyStartGame(gameSession.getPlayerOne());
        webSocketService.notifyStartGame(gameSession.getPlayerTwo());
    }


    public void sendState(GameSession gameSession, long timeTick) {
        if (gameSession != null) {
            final GameFrame gameFrame = gameSession.getGameFrame();
            gameSession.moveBullets(timeTick);

            final String gameFrameJson = gson.toJson(gameFrame);
            webSocketService.sendMessageToUser(gameFrameJson, gameSession.getPlayerOne());

            PlayerObject player = gameFrame.getEnemy();
            gameFrame.setEnemy(gameFrame.getPlayer());
            gameFrame.setPlayer(player);
            gameFrame.toAnotherCoordinateSystem(xMax, yMax);

            final String gameFrameJsonSecond = gson.toJson(gameFrame);

            player = gameFrame.getEnemy();
            gameFrame.setEnemy(gameFrame.getPlayer());
            gameFrame.setPlayer(player);
            gameFrame.toAnotherCoordinateSystem(xMax, yMax);

            webSocketService.sendMessageToUser(gameFrameJsonSecond, gameSession.getPlayerTwo());
        }
    }

    @Override
    public void changeState(GameChanges gameChanges, String userName) {
        tasks.add(() -> changeStateInternal(gameChanges, userName));
    }

    private void changeStateInternal(GameChanges gameChanges, String userName) {
        final GameSession gameSession = nameToGame.get(userName);
        if (gameSession != null) {
            final PlayerObject playerObject = gameChanges.getPlayer();
            final GameFrame gameFrame = gameSession.getGameFrame();
            if (gameSession.getPlayerOne().equals(userName)) {
                final Bullet bullet = gameChanges.getBullet();
                if (bullet != null) {
                    bullet.toAnotherCoordinateSystem(xMax, yMax);
                    gameChanges.setBullet(bullet);
                }
                if (playerObject != null) {
                    playerObject.toAnotherCoordinateSystem(xMax);
                    playerObject.translateGunAgnle();
                    gameFrame.setEnemy(playerObject);
                }
            } else {
                if (playerObject != null) {
                    playerObject.translateGunAgnle();
                    gameFrame.setPlayer(playerObject);
                }
            }
            gameSession.changeState(gameChanges);
        }
    }

    @Override
    public void closeGameSession(String user) {
        final GameSession gameSession = nameToGame.remove(user);
        if (gameSession != null) {
            nameToGame.remove(gameSession.getPlayerTwo());
            allSessions.remove(gameSession);
        } else {
            waiter = null;
        }
    }
}
