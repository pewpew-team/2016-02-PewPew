package com.pewpew.pewpew.websoket;

import com.pewpew.pewpew.model.User;

public interface WebSocketService {

    void addUser(User user);

//    void notifyMyNewScore(User user);
//
//    void notifyEnemyNewScore(User user);

    void notifyStartGame(User user);

    void notifyGameOver(User user, boolean win);
}

