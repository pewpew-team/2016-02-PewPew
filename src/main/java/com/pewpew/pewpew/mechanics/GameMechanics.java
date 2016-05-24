package com.pewpew.pewpew.mechanics;

import com.pewpew.pewpew.messagesystem.Address;
import com.pewpew.pewpew.model.GameChanges;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;

public interface GameMechanics {
    void addUser(@NotNull String user, @NotNull Address addressSocket);

    String getEnemy(@NotNull String user);

    void changeState(GameChanges gameChanges, String userName);

    void run();

    void start();

    void removeSession(Session session);

    void closeGameSession(String user);

    void pauseGame(String dissconectedUser);

    Address getAddress();
}
