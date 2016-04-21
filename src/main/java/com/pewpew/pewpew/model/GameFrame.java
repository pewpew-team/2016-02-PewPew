package com.pewpew.pewpew.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameFrame {
    private PlayerObject player;
    private PlayerObject enemy;
    private List<Bullet> bullets;
    private List<Barrier> barriers;

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

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void addBullet(Bullet bullet) {
        if (this.bullets.size() == 0 ) {
            bullet.setBulletId(0);
        } else {
            bullet.setBulletId(this.bullets.get(this.bullets.size() - 1).getBulletId() + 1);
        }
        this.bullets.add(bullet);
    }

    public List<Barrier> getBarriers() {
        return barriers;
    }

    public void setBarriers(List<Barrier> barriers) {
        this.barriers = barriers;
    }

    public void translateToAnotherCoordinateSystem(Double x, Double y) {
        player.translateToAnotherCoordinateSystem(x);
        enemy.translateToAnotherCoordinateSystem(x);
        for(Bullet bullet : bullets) {
            bullet.translateToAnotherCoordinateSystem(x, y);
        }
    }

    public void moveBullets() {
        Iterator<Bullet> i = bullets.iterator();
        while (i.hasNext()) {
            Bullet bullet = i.next();
            bullet.setPosX(bullet.getPosX() + bullet.getVelX());
            bullet.setPosY(bullet.getPosY() + bullet.getVelY());
            if (bullet.getPosX() < 0 || bullet.getPosX() > X_MAX) {
                bullet.setVelX(-1 * bullet.getVelX());
            }
            if (bullet.getPosY() > Y_MAX || bullet.getPosY() < 0) {
                i.remove();
                System.out.println("removed bullet");
            }
        }
    }


}
