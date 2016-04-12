package com.pewpew.pewpew.websoket;

import com.pewpew.pewpew.model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WebSocketServiceImpl implements WebSocketService {
    private Map<String, GameWebSocket> userSockets = new HashMap<>();
    private static WebSocketService instance;

    public static synchronized WebSocketService getInstance() {
        if (instance == null) {
            instance = new WebSocketServiceImpl();
        }
        return instance;
    }

    @Override
    public void addUser(GameWebSocket user, String userId) {
        userSockets.put(userId, user);
    }

    @Override
    public void notifyStartGame(String user) {
        GameWebSocket gameWebSocket = userSockets.get(user);
        gameWebSocket.startGame();
    }

    @Override
    public void sendMessageToUser(String message, String userSessionId) throws IOException {
        GameWebSocket gameWebSocket = userSockets.get(userSessionId);
        if (gameWebSocket == null) {
            throw new IOException("no such active websocket");
        }
    }

    @Override
    public void notifyGameOver(User user, boolean win) {

    }
}
