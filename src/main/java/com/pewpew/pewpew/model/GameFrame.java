package com.pewpew.pewpew.model;

import java.util.ArrayList;
import java.util.List;

public class GameFrame {
    private PlayerObject player;
    private PlayerObject enemy;
    private final List<Bullet> bullets;
    private List<Barrier> barriers;

    public GameFrame(PlayerObject player, PlayerObject enemy) {
        this.player = player;
        this.enemy = enemy;
        this.bullets = new ArrayList<>();
        this.barriers = new ArrayList<>();
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
        if (this.bullets.isEmpty()) {
            bullet.setBulletId(0);
        } else {
            bullet.setBulletId(this.bullets.get(this.bullets.size() - 1).getBulletId() + 1);
        }
        this.bullets.add(bullet);
    }

    public void addBarriers(Barrier barrier) {
        this.barriers.add(barrier);
    }

    public List<Barrier> getBarriers() {
        return barriers;
    }

    public void setBarriers(List<Barrier> barriers) {
        this.barriers = barriers;
    }

    public void toAnotherCoordinateSystem(Double x, Double y) {
        player.toAnotherCoordinateSystem(x);
        enemy.toAnotherCoordinateSystem(x);
        bullets.forEach(bullet -> bullet.toAnotherCoordinateSystem(x, y));
        barriers.forEach(barrier -> barrier.toAnotherCoordinateSystem(x, y));
    }


}
