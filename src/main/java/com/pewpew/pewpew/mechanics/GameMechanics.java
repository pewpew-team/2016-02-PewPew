package com.pewpew.pewpew.mechanics;

import org.jetbrains.annotations.NotNull;

public interface GameMechanics {
    void addUser(@NotNull String user);

    String getEnemy(@NotNull String user);
}
