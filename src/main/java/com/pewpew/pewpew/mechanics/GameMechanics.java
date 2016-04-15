package com.pewpew.pewpew.mechanics;

import com.pewpew.pewpew.model.BulletObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public interface GameMechanics {
    void addUser(@NotNull String user);

    String getEnemy(@NotNull String user);

    ArrayList<BulletObject> bulletsCalculation(BulletObject bullet, String user);
}
