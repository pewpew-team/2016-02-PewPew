package com.pewpew.pewpew.model;

public class GameChanges {
    private BulletObject bullet;
    private String playerEvent;

    public BulletObject getBullet() {
        return bullet;
    }

    public void setBullet(BulletObject bullet) {
        this.bullet = bullet;
    }

    public String getPlayerEvent() {
        return playerEvent;
    }

    public void setPlayerEvent(String playerEvent) {
        this.playerEvent = playerEvent;
    }
}
