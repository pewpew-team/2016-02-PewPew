package com.pewpew.pewpew.model;

public class GameChanges {
    private Bullet bullet;
    private PlayerObject player;

    public Bullet getBullet() {
        return bullet;
    }

    public void setBullet(Bullet bullet) {
        this.bullet = bullet;
    }

    public PlayerObject getPlayer() {
        return player;
    }

    public void setPlayer(PlayerObject player) {
        this.player = player;
    }
}
