package com.pewpew.pewpew.websoket;

import com.pewpew.pewpew.model.User;

import javax.inject.Singleton;

@Singleton
public interface WebSocketService {

    void addUser(GameWebSocket user, String userId);

//    void notifyMyNewScore(User user);
//
//    void notifyEnemyNewScore(User user);

    void notifyStartGame(String user);

    void notifyGameOver(User user, boolean win);
}

