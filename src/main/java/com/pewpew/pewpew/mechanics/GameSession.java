package com.pewpew.pewpew.mechanics;

import com.pewpew.pewpew.model.BulletObject;
import com.pewpew.pewpew.model.GameChanges;
import com.pewpew.pewpew.model.GameFrame;
import com.pewpew.pewpew.model.PlayerObject;

import java.time.Clock;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameSession {

    private static final Double Y_MAX = 720.0;
    private static final Double X_MAX = 1280.0;

    private String playerOne;
    private String playerTwo;

    private GameFrame gameFrame;

    private final long startTime;

    private ConcurrentLinkedQueue<BulletObject> bulletObjects = new ConcurrentLinkedQueue<BulletObject>();

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

    public GameFrame getGameFrame() {
        return gameFrame;
    }

    public void setGameFrame(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    public GameSession(String playerOne, String playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        startTime = Clock.systemDefaultZone().millis();
        PlayerObject playerFirst = new PlayerObject();
        PlayerObject playerSecond = new PlayerObject();
        playerFirst.setPosX(640.0);
        playerSecond.setPosX(640.0);

        gameFrame = new GameFrame(playerFirst, playerSecond);
    }

    public ConcurrentLinkedQueue<BulletObject> getBulletObjects() {
        return bulletObjects;
    }
    public void setBulletObject(BulletObject bulletObjects) {
        this.bulletObjects.add(bulletObjects);
    }

    public long getSessionTime(){
        return Clock.systemDefaultZone().millis() - startTime;
    }

    public void changeState(GameChanges gameChanges) {
        System.out.print("Gonna change if i got bullet \n");
        BulletObject bulletObject = gameChanges.getBullet();
        if (bulletObject != null) {
           gameFrame.addBullet(bulletObject);
            System.out.print("I added bullet, i am so cool \n");
            System.out.print(gameFrame.getBullets().size());
        }
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
        try {
            Double newX = bulletObject.getPosX() + bulletObject.getVelX();
            Double newY = bulletObject.getPosY() + bulletObject.getVelY();
            bulletObject.setPosX(newX);
            bulletObject.setPosY(newY);
        } catch ( NullPointerException e) {
            e.printStackTrace();
            System.out.print(bulletObject);
        }
    }
}
