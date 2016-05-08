package com.pewpew.pewpew.mechanics;

import com.pewpew.pewpew.common.Point;
import com.pewpew.pewpew.model.*;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GameSession {

    private final Double yMax;
    private final Double xMax;

    private String playerOne;
    private String playerTwo;

    private Boolean playerOneWon;

    private GameFrame gameFrame;

    private final ArrayList<Bullet> bulletList = new ArrayList<>();

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

    public GameSession(String playerOne, String playerTwo, Double xMax, Double yMax) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        final PlayerObject playerFirst = new PlayerObject();
        final PlayerObject playerSecond = new PlayerObject();

        gameFrame = new GameFrame(playerFirst, playerSecond);
        final Properties property = new Properties();
        final String path = new File("").getAbsolutePath() + "/resources/game.properties";
        try(FileInputStream fileInputStream =
                    new FileInputStream(path)) {
            property.load(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Can't handle game resourses");
        }
        final Integer nX = Integer.valueOf(property.getProperty("block.nX"));
        final Double ratio = Double.valueOf(property.getProperty("block.ratio"));
        final Integer x0 = Integer.valueOf(property.getProperty("block.x0"));
        final Integer y0 = Integer.valueOf(property.getProperty("block.y0"));
        putBlocks(nX, 4, ratio, x0, y0);

        this.xMax = xMax;
        this.yMax = yMax;
    }

    public ArrayList<Bullet> getBulletObjects() {
        return bulletList;
    }

    public void setBulletObject(Bullet bulletObjects) {
        this.bulletList.add(bulletObjects);
    }


    public void changeState(GameChanges gameChanges) {
        final Bullet bullet = gameChanges.getBullet();
        if (bullet != null) {
           gameFrame.addBullet(bullet);
        }
    }

    public void moveBullets(long timeBefore) {
        final Iterator<Bullet> iterator = gameFrame.getBullets().iterator();
        final Rectangle userOne = gameFrame.getPlayer().getRect(yMax.intValue());
        final Rectangle userTwo = gameFrame.getEnemy().getRectEnemy();
        while (iterator.hasNext()) {
            final Bullet bullet = iterator.next();
            bullet.setPosX(bullet.getPosX() + bullet.getVelX() * timeBefore);
            bullet.setPosY(bullet.getPosY() + bullet.getVelY() * timeBefore);
            if (bullet.getPosX() < 0 || bullet.getPosX() > xMax) {
                bullet.setVelX(-1 * bullet.getVelX());
            }
            System.out.println("userOne pos: " +userOne + " and bullet with id"
                    + bullet.getBulletId() + " with pos: " + bullet.getRect());
            if (userOne.contains(bullet.getRect())) {
                playerOneWon = true;
            }
            System.out.println("userTow pos: " + userTwo + " and bullet with id"
                    + bullet.getBulletId() + " with pos: " + bullet.getRect());
            if (userTwo.contains(bullet.getRect())) {
                playerOneWon = false;
            }
            System.out.println();
            if (bullet.getPosY() > yMax || bullet.getPosY() < 0) {
                System.out.println("removed bullet: " + bullet.getBulletId());
                iterator.remove();
            }
//            gameFrame.getBarriers().stream().filter(barrier -> tryToCollide(bullet, barrier)).forEach(barrier -> {
//                collide(bullet, barrier);
//            });
            Iterator<Barrier> barrierIterator = gameFrame.getBarriers().iterator();
            while (barrierIterator.hasNext()){
                final Barrier barrier = barrierIterator.next();
                if (tryToCollide(bullet, barrier)) {
                    collide(bullet,barrier);
                    if(barrier.isRemovable()){
                        barrierIterator.remove();
                    }
                }
            }
        }
    }

    public void putBlocks(Integer nX, Integer nY, Double ratio, Integer x0, Integer y0) {
        Integer max = 100;
        Integer period = 50;
        for (Integer i = 0; i < nX; ++i) {
            for (Integer j = 0; j < nY; ++j) {
                Barrier barrier = new Barrier();
                barrier.setPosX((double) (i * period + x0));
                barrier.setPosY((double)(j * period + y0));

                Double randomNumber = (Math.random() * (max + 1));

                barrier.setRemovable(true);
                if (randomNumber < max * ratio) {
                    barrier.setRemovable(false);
                }
                gameFrame.addBarriers(barrier);
            }
        }
    }

    public void handleCollision() {
        //TODO: write collision handler or change moveBullets method
    }

    private Boolean tryToCollide(Bullet bullet, Barrier barrier) {
        final Double collisionDistX = Math.pow(((bullet.getSizeX() + barrier.getSizeX())/2), 2);
        final Double collisionDistY = Math.pow(((bullet.getSizeY() + barrier.getSizeY())/2), 2);

        final Double distSquare = Math.pow((bullet.getPosX() - barrier.getPosX()),2) +
                Math.pow((bullet.getPosY() - barrier.getPosY()), 2);

        return ((collisionDistX > distSquare) || (collisionDistY > distSquare));
    }

    private void collide(Bullet bullet, Barrier barrier) {
        final Double bulletPosX = bullet.getPosX() - (barrier.getPosX() - barrier.getSizeX()/2);
        final Double bulletPosY = bullet.getPosY() - (barrier.getPosY() - barrier.getSizeY()/2);

        final Double k = bullet.getVelY() / bullet.getVelX();
        final Double b = bulletPosY - k * bulletPosX;
        final Random rand = new Random();


        //TODO: Refactoring. Remove temporary wariables.
        double tempX = (bullet.getVelX() > 0)? 0 : barrier.getSizeX();
        double tempY = k * tempX + b;
        final Point intersectionWithParallelX = new Point(tempX, tempY);

        tempY = (bullet.getVelY() > 0)? 0 : barrier.getSizeY();
        tempX = (tempY - b) / k;
        final Point intersectionWithParallelY = new Point(tempX, tempY);

        final Double fault = 3.0;
        final Double absoluteDeviation = 0.5;
        final Double deviation = (rand.nextBoolean())? absoluteDeviation: -absoluteDeviation;
        
        if(Math.abs(intersectionWithParallelY.getX() - intersectionWithParallelX.getX()) < fault) {
            this.moveToIntersectionPoint(bullet, barrier, intersectionWithParallelX);
            bullet.setVelX(-bullet.getVelX() + deviation);
            bullet.setVelY(-bullet.getVelY() + deviation);
        }
        else if ((intersectionWithParallelX.getX() >= 0) && (intersectionWithParallelX.getX() <= barrier.getSizeX())) {
            moveToIntersectionPoint(bullet, barrier, intersectionWithParallelX);
            bullet.setVelY(-bullet.getVelY() + deviation);
        }
        else if ((intersectionWithParallelY.getX() >= 0) && (intersectionWithParallelY.getX() <= barrier.getSizeY())) {
            moveToIntersectionPoint(bullet, barrier, intersectionWithParallelY);
            bullet.setVelX(-bullet.getVelX() + deviation);
        }
    }

    private void moveToIntersectionPoint(Bullet bullet, Barrier barrier, Point intersectionPoint) {
        bullet.setPosX(intersectionPoint.getX() + (barrier.getPosX() - barrier.getSizeX()/2));
        bullet.setPosY(intersectionPoint.getY() + (barrier.getPosY() - barrier.getSizeY()/2));
    }


}
