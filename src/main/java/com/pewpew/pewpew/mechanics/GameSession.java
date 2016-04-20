package com.pewpew.pewpew.mechanics;

import com.pewpew.pewpew.model.BulletObject;
import com.pewpew.pewpew.model.GameChanges;
import com.pewpew.pewpew.model.GameFrame;
import com.pewpew.pewpew.model.PlayerObject;

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
//        final PlayerObject playerObject = gameChanges.getPlayer();
//        if (playerObject != null) {
////            playerObject.toAnotherCoordinateSystem(X_MAX);
//            playerObject.translateGunAgnle();
//            gameFrame.setEnemy(playerObject);
//        }
    }

    public void moveBullets() {
        final Iterator<BulletObject> iterator = gameFrame.getBullets().iterator();
        while (iterator.hasNext()) {
            final BulletObject bulletObject = iterator.next();
            bulletObject.setPosX(bulletObject.getPosX() + bulletObject.getVelX());
            bulletObject.setPosY(bulletObject.getPosY() + bulletObject.getVelY());
            if (bulletObject.getPosX() < 0 || bulletObject.getPosX() > X_MAX) {
                bulletObject.setVelX(-1 * bulletObject.getVelX());
            }
            if (bulletObject.getPosY() > Y_MAX || bulletObject.getPosY() < 0) {
                iterator.remove();
                System.out.println("removed bullet");
            }
        }
    }
}
