package com.pewpew.pewpew.websoket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WebSocketServiceImpl implements WebSocketService {
    private final Map<String, GameWebSocket> userSockets = new HashMap<>();

    @Override
    public void addUser(GameWebSocket user, String userId) {
        userSockets.put(userId, user);
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
        gameWebSocket.sendMessage(message);
    }

    @Override
    public void notifyGameOver(String user, Boolean win) {
        final GameWebSocket gameWebSocket = userSockets.get(user);
        if (gameWebSocket != null) {
            if (win) {
                gameWebSocket.sendMessage("You win");
            } else {
                gameWebSocket.sendMessage("You lose");
            }
        }
    }
}
