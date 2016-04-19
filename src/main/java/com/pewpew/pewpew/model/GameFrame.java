package com.pewpew.pewpew.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameFrame {
    private PlayerObject player;
    private PlayerObject enemy;
    private List<BulletObject> bullets;
    private List<BarriersObject> barriers;

    private static final Double Y_MAX = 720.0;
    private static final Double X_MAX = 1280.0;

    public GameFrame(PlayerObject player, PlayerObject enemy) {
        this.player = player;
        this.enemy = enemy;
        this.bullets = new ArrayList<>();
    }

    public PlayerObject getPlayer() {
        return player;
    }

    public void setPlayer(PlayerObject player) {
        this.player = player;
    }

    public PlayerObject getEnemy() {
        return enemy;
    }

    public void setEnemy(PlayerObject enemy) {
        this.enemy = enemy;
    }

    public List<BulletObject> getBullets() {
        return bullets;
    }

    public void addBullet(BulletObject bullet) {
        if (this.bullets.size() == 0 ) {
            bullet.setBulletId(0);
        } else {
            bullet.setBulletId(this.bullets.get(this.bullets.size() - 1).getBulletId() + 1);
        }
        this.bullets.add(bullet);
    }

    public List<BarriersObject> getBarriers() {
        return barriers;
    }

    public void setBarriers(List<BarriersObject> barriers) {
        this.barriers = barriers;
    }

    public void translateToAnotherCoordinateSystem(Double x, Double y) {
        player.translateToAnotherCoordinateSystem(x);
        enemy.translateToAnotherCoordinateSystem(x);
        for(BulletObject bullet : bullets) {
            bullet.translateToAnotherCoordinateSystem(x, y);
        }
    }

    public void moveBullets() {
        Iterator<BulletObject> i = bullets.iterator();
        while (i.hasNext()) {
            BulletObject bulletObject = i.next();
            bulletObject.setPosX(bulletObject.getPosX() + bulletObject.getVelX());
            bulletObject.setPosY(bulletObject.getPosY() + bulletObject.getVelY());
            if (bulletObject.getPosX() < 0 || bulletObject.getPosX() > X_MAX) {
                bulletObject.setVelX(-1 * bulletObject.getVelX());
            }
            if (bulletObject.getPosY() > Y_MAX || bulletObject.getPosY() < 0) {
                i.remove();
                System.out.println("removed bullet");
            }
        }
    }
}
