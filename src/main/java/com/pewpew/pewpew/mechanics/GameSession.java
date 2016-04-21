package com.pewpew.pewpew.mechanics;

import com.pewpew.pewpew.common.*;
import com.pewpew.pewpew.common.Point;
import com.pewpew.pewpew.model.*;

import java.awt.*;
import java.time.Clock;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameSession {
    private String playerOne;
    private String playerTwo;

    private GameFrame gameFrame;

    private final long startTime;

    private ConcurrentLinkedQueue<Bullet> bullets = new ConcurrentLinkedQueue<Bullet>();

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

    public ConcurrentLinkedQueue<Bullet> getBullets() {
        return bullets;
    }
    public void setBulletObject(Bullet bulletObjects) {
        this.bullets.add(bulletObjects);
    }

    public long getSessionTime(){
        return Clock.systemDefaultZone().millis() - startTime;
    }

    public void changeState(GameChanges gameChanges) {
        System.out.print("Gonna change if i got bullet \n");
        Bullet bullet = gameChanges.getBullet();
        if (bullet != null) {
           gameFrame.addBullet(bullet);
            System.out.print("I added bullet, i am so cool \n");
        }
    }

    public void handleCollision() {
        List<Bullet> Bullet = gameFrame.getBullets();
        List<Barrier> Barriers = gameFrame.getBarriers();



    }

    private Boolean tryToCollide(Bullet bullet, Barrier barrier) {
        final Double collisionDistX = Math.pow(((bullet.getSizeX() + barrier.getSizeX())/2), 2);
        final Double collisionDistY = Math.pow(((bullet.getSizeY() + barrier.getSizeY())/2), 2);

        final Double distSquare = Math.pow((bullet.getPosX() - barrier.getPosX()),2) +
                Math.pow((bullet.getPosY() - barrier.getPosY()), 2);

        return ((collisionDistX > distSquare) || (collisionDistY > distSquare));
    }

    private void collide(Bullet bullet, Barrier barrier) {
        Double bulletPosX = bullet.getPosX() - (barrier.getPosX() - barrier.getSizeX()/2);
        Double bulletPosY = bullet.getPosY() - (barrier.getPosY() - barrier.getSizeY()/2);

        Double k = bullet.getVelY() / bullet.getVelX();
        Double b = bulletPosY - k * bulletPosX;
        final Double fault =  3.0;
        final Double absoluteDeviation = 0.5;
        Random rand = new Random();
        Double deviation = (rand.nextBoolean())? absoluteDeviation: -absoluteDeviation;

        double tempX = (bullet.getVelX() > 0)? 0 : barrier.getSizeX();
        double tempY = k * tempX + b;
        Point intersectionWithParallelX = new Point(tempX, tempY);

        tempY = (bullet.getVelY() > 0)? 0 : barrier.getSizeY();
        tempX = (tempY - b) / k;
        Point intersectionWithParallelY = new Point(tempX, tempY);

        if(Math.abs(intersectionWithParallelY.getX() - intersectionWithParallelX.getX()) < fault) {
            bullet.
        }

    }

    private void moveToIntersectionPoint(Bullet bullet, Barrier barrier, Point intersectionPoint) {

    }
}
