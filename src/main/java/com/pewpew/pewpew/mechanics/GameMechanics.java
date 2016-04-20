package com.pewpew.pewpew.mechanics;

import com.pewpew.pewpew.model.GameChanges;
import org.jetbrains.annotations.NotNull;

public interface GameMechanics {
    void addUser(@NotNull String user);

    String getEnemy(@NotNull String user);

    void changeState(GameChanges gameChanges, String userName);

    void run();
}
