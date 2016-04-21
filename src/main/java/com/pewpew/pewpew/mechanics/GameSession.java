package com.pewpew.pewpew.mechanics;

import com.pewpew.pewpew.common.*;
import com.pewpew.pewpew.common.Point;
import com.pewpew.pewpew.model.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class GameSession {

    private static final Double Y_MAX = 720.0;
    private static final Double X_MAX = 1280.0;

    private String playerOne;
    private String playerTwo;

    private Boolean playerOneWon;

    private GameFrame gameFrame;

    private final ArrayList<BulletObject> bulletObjects = new ArrayList<>();

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

    public Boolean getPlayerOneWon() {
        return playerOneWon;
    }

    public GameSession(String playerOne, String playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        final PlayerObject playerFirst = new PlayerObject();
        final PlayerObject playerSecond = new PlayerObject();

        gameFrame = new GameFrame(playerFirst, playerSecond);
    }

    public ArrayList<BulletObject> getBulletObjects() {
        return bulletObjects;
    }

    public void setBulletObject(BulletObject bulletObjects) {
        this.bulletObjects.add(bulletObjects);
    }


    public void changeState(GameChanges gameChanges) {
        final BulletObject bulletObject = gameChanges.getBullet();
        if (bulletObject != null) {
           gameFrame.addBullet(bulletObject);
        }
    }

    public void moveBullets(long timeTick) {
        final Iterator<BulletObject> iterator = gameFrame.getBullets().iterator();
        final Rectangle userOne = gameFrame.getPlayer().getRect();
        final Rectangle userTwo = gameFrame.getEnemy().getRect();
        while (iterator.hasNext()) {
            final BulletObject bulletObject = iterator.next();
            bulletObject.setPosX(bulletObject.getVelX() * timeTick);
            bulletObject.setPosY(bulletObject.getVelY() * timeTick);
            if (bulletObject.getPosX() < 0 || bulletObject.getPosX() > X_MAX) {
                bulletObject.setVelX(-1 * bulletObject.getVelX());
            }
            if (userOne.contains(bulletObject.getRect())) {
                playerOneWon = true;
            }
            if (userTwo.contains(bulletObject.getRect())) {
                playerOneWon = false;
            }
            if (bulletObject.getPosY() > Y_MAX || bulletObject.getPosY() < 0) {
                iterator.remove();
                System.out.println("removed bullet");
            }
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
