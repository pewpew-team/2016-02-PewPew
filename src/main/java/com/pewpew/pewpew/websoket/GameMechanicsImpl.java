package com.pewpew.pewpew.websoket;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Singleton;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Singleton
public class GameMechanicsImpl {

    private static GameMechanicsImpl instance;

    @NotNull
    private WebSocketService webSocketService;

    @NotNull
    private Set<GameSession> allSessions = new HashSet<>();

    @NotNull
    private Map<String, GameSession> nameToGame = new HashMap<>();

    @Nullable
    private volatile String waiter;

    @NotNull
    private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();

    public static synchronized GameMechanicsImpl getInstance() {
        if (instance == null) {
            instance = new GameMechanicsImpl();
        }
        return instance;
    }

    private GameMechanicsImpl() {
        webSocketService = WebSocketServiceImpl.getInstance();
    }

    public void addUser(@NotNull String user) {
        addUserInternal(user);
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

        webSocketService.notifyStartGame(gameSession.getPlayerOne());
        webSocketService.notifyStartGame(gameSession.getPlayerTwo());
    }
}
