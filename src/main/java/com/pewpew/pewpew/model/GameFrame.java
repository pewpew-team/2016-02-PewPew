package com.pewpew.pewpew.model;

import java.util.ArrayList;
import java.util.List;

public class GameFrame {
    private PlayerObject player;
    private PlayerObject enemy;
//    private BulletsCollection bullets;
    private List<BulletObject> bullets;
    private BulletObject bullet;
    private List<BarriersObject> barriers;

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

//    public BulletsCollection getBullets() {
//        return bullets;
//    }
//
//    public void setBullets(BulletsCollection bullets) {
//        this.bullets = bullets;
//    }


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

    public List<BarriersObject> getBarriers() {
        return barriers;
    }

    public void setBarriers(List<BarriersObject> barriers) {
        this.barriers = barriers;
    }
}
