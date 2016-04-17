package com.pewpew.pewpew.model;

import java.util.List;

public class GameFrame {
    private PlayerObject player;
    private PlayerObject enemy;
    private List<BulletObject> bullets;
    private BulletObject bullet;
    private List<BarriersObject> barriers;

    public GameFrame(PlayerObject player, PlayerObject enemy) {
        this.player = player;
        this.enemy = enemy;
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

    public BulletObject getBullet() {
        return bullet;
    }

    public void setBullet(BulletObject bullet) {
        this.bullet = bullet;
    }

    public List<BulletObject> getBullets() {
        return bullets;
    }

    public void setBullets(List<BulletObject> bullets) {
        this.bullets = bullets;
    }

    public void addBullets(List<BulletObject> bullets) {
        this.bullets.addAll(bullets);
    }

    public List<BarriersObject> getBarriers() {
        return barriers;
    }

    public void setBarriers(List<BarriersObject> barriers) {
        this.barriers = barriers;
    }

    public void translateToAnotherCoordinateSystem(Double x, Double y) {
        player.translateToAnotherCoordinateSystem(x);
        enemy.translateToAnotherCoordinateSystem(y);
        for(BulletObject bullet : bullets) {
            bullet.translateToAnotherCoordinateSystem(x, y);
        }
    }
}
