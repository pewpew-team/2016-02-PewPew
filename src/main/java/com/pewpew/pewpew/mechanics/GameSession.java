package com.pewpew.pewpew.mechanics;

import com.pewpew.pewpew.model.BulletObject;

import java.time.Clock;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameSession {

    private String playerOne;
    private String playerTwo;

    private final long startTime;

    private CopyOnWriteArrayList<BulletObject> bulletObjects = new CopyOnWriteArrayList<BulletObject>();

    public String getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(String playerOne) {
        this.playerOne = playerOne;
    }

    public String getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(String playerTwo) {
        this.playerTwo = playerTwo;
    }

    public GameSession(String playerOne, String playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        startTime = Clock.systemDefaultZone().millis();
    }

    public CopyOnWriteArrayList<BulletObject> getBulletObjects() {
        return bulletObjects;
    }
    public void setBulletObject(BulletObject bulletObjects) {
        this.bulletObjects.add(bulletObjects);
    }

    public long getSessionTime(){
        return Clock.systemDefaultZone().millis() - startTime;
    }

    public ArrayList<BulletObject> getAllBullets() {
        ArrayList<BulletObject> resultBulllets = new ArrayList<>();
        for (BulletObject bullet : bulletObjects) {
            moveBullet(bullet);
            resultBulllets.add(bullet);
        }
        return resultBulllets;
    }

    private void moveBullet(BulletObject bulletObject) {
        Double newX = bulletObject.getPosX() + bulletObject.getVelX();
        Double newY = bulletObject.getPosY() + bulletObject.getVelY();
        bulletObject.setPosX(newX);
        bulletObject.setPosY(newY);
    }
}
