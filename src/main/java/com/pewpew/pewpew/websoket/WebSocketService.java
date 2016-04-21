package com.pewpew.pewpew.websoket;

import javax.inject.Singleton;

@Singleton
public interface WebSocketService {

    void addUser(GameWebSocket user, String userId);

//    void notifyMyNewScore(User user);
//
//    void notifyEnemyNewScore(User user);

    void notifyStartGame(String user);

    void sendMessageToUser(String message, String userSessionId);

    void notifyGameOver(String user, Boolean win);
}

