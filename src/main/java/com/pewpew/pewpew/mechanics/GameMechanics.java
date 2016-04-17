package com.pewpew.pewpew.mechanics;

import com.pewpew.pewpew.model.BulletObject;
import com.pewpew.pewpew.model.GameFrame;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public interface GameMechanics {
    void addUser(@NotNull String user);

    String getEnemy(@NotNull String user);

    ArrayList<BulletObject> bulletsCalculation(String user);
    void addNewBullet(BulletObject bullet, String user);
    void changeState(GameFrame gameFrame, String userName);
}
