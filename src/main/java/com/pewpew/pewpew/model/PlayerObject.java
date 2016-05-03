package com.pewpew.pewpew.model;

import java.awt.*;

public class PlayerObject {
    public static final double PI = 3.14;
    public static final double START_POSITION_X = 640.0;
    private Double posX;
    private Double velX;
    private Double gunAngle;

    private static final Integer WIDTH = 80;
    private static final Integer HEIGHT = 50;

    public Rectangle getRect(Integer yMax) {
        return new Rectangle(posX.intValue() - WIDTH/2, yMax - HEIGHT/2, WIDTH, HEIGHT);
    }
    public Rectangle getRectEnemy() {
        return new Rectangle(posX.intValue() - WIDTH/2, HEIGHT/2, WIDTH, HEIGHT);
    }

    public PlayerObject() {
        this.posX = START_POSITION_X;
        this.velX = 0.0;
    }

    public Double getPosX() {
        return posX;
    }

    public void setPosX(Double posX) {
        this.posX = posX;
    }

    public Double getVelX() {
        return velX;
    }

    public void setVelX(Double velX) {
        this.velX = velX;
    }

    public void toAnotherCoordinateSystem(Double x) {
        posX = x - posX;
        velX = -velX;
    }

    public void translateGunAgnle() {
        gunAngle = PI + gunAngle;
    }
}
