package com.pewpew.pewpew.websoket;

import javax.inject.Singleton;

@Singleton
public interface WebSocketService {

    void addUser(GameWebSocket user, String userId);

    void removeUser(String userId);

    Boolean containsUser(String userId);

    void notifyStartGame(String user, String enemy);

    void sendMessageToUser(String message, String userSessionId);

    void notifyGameOver(String user, Boolean win);

}

