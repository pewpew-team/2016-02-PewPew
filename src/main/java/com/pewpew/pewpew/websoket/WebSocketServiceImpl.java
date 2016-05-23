package com.pewpew.pewpew.websoket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class WebSocketServiceImpl implements WebSocketService {
    private final Map<String, GameWebSocket> userSockets = new HashMap<>();

    @Override
    public void addUser(GameWebSocket gameWebSocket, String userId) {
        userSockets.put(userId, gameWebSocket);
    }

    public void removeUser(String userId) {
        userSockets.remove(userId);
    }
    @Override
    public void notifyStartGame(String user) {
        final GameWebSocket gameWebSocket = userSockets.get(user);
        gameWebSocket.startGame();
    }

    @Override
    public void sendMessageToUser(String message, String userSessionId) {
        final GameWebSocket gameWebSocket = userSockets.get(userSessionId);
        if (gameWebSocket == null) {
            return;
        }
        if (gameWebSocket.getUserSession() == null) {
            userSockets.remove(userSessionId);
        }
        gameWebSocket.setGameEnded(true);
        gameWebSocket.sendMessage(message);
    }

    @Override
    public void notifyGameOver(String user, Boolean win) {
        final GameWebSocket gameWebSocket = userSockets.get(user);
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("win", win);
        final Gson gson = new Gson();
        gameWebSocket.sendMessage(gson.toJson(jsonObject));
    }
}
