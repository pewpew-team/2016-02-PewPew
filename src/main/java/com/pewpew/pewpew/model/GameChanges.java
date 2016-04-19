package com.pewpew.pewpew.model;

public class GameChanges {
    private BulletObject bullet;
    private PlayerObject player;

    public BulletObject getBullet() {
        return bullet;
    }

    public void setBullet(BulletObject bullet) {
        this.bullet = bullet;
    }

    public PlayerObject getPlayer() {
        return player;
    }

    public void setPlayer(PlayerObject player) {
        this.player = player;
    }
}
