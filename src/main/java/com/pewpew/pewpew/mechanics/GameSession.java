package com.pewpew.pewpew.mechanics;

import com.pewpew.pewpew.model.BulletObject;
import com.pewpew.pewpew.model.GameFrame;
import com.pewpew.pewpew.model.PlayerObject;

import java.time.Clock;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameSession {

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

    public ArrayList<BulletObject> getAllBullets() {
        ArrayList<BulletObject> resultBulllets = new ArrayList<>();
        for (BulletObject bullet : bulletObjects) {
            moveBullet(bullet);
            resultBulllets.add(bullet);
            System.out.print(bullet.getPosX() + "\n");
            System.out.print(bullet.getPosY() + "\n");
            System.out.print(bullet.getVelX() + "\n");
            System.out.println();
            System.out.println();
        }
        return resultBulllets;
    }

    private void moveBullet(BulletObject bulletObject) {
        Double newX = bulletObject.getPosX() + bulletObject.getVelX();
        Double newY = bulletObject.getPosY() + bulletObject.getVelY();
        bulletObject.setPosX(newX);
        bulletObject.setPosY(newY);
    }

    public void translateToAnotherCoordinates() {
    }
}
