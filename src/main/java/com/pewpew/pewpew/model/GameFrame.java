package com.pewpew.pewpew.model;

import java.util.ArrayList;
import java.util.List;

public class GameFrame {
    private PlayerObject player;
    private PlayerObject enemy;
    private List<BulletObject> bullets;
    private List<BarriersObject> barriers;

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
        bullet.setBulletId(this.bullets.size());
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
        for(BulletObject bulletObject : bullets) {
            bulletObject.setPosX(bulletObject.getPosX() + bulletObject.getVelX());
            bulletObject.setPosY(bulletObject.getPosY() + bulletObject.getVelY());
        }
    }
}
