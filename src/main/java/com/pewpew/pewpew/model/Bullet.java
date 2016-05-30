package com.pewpew.pewpew.model;

import java.awt.*;

public class Bullet {

    private Integer bulletId = 0;
    private Double posX;
    private Double posY;
    private Double velX;
    private Double velY;
    private Double sizeX;
    private Double sizeY;

    private transient Integer collisions = 0;

    public Integer getCollisions() {
        return collisions;
    }

    public void setCollisions(Integer collisions) {
        this.collisions = collisions;
    }

    public Integer getBulletId() {
        return bulletId;
    }

    public void setBulletId(Integer bulletId) {
        this.bulletId = bulletId;
    }

    public Double getPosX() {
        return posX;
    }

    public void setPosX(Double posX) {
        this.posX = posX;
    }

    public Double getPosY() {
        return posY;
    }

    public void setPosY(Double posY) {
        this.posY = posY;
    }

    public Double getVelX() {
        return velX;
    }

    public void setVelX(Double velX) {
        this.velX = velX;
    }

    public Double getVelY() {
        return velY;
    }

    public void setVelY(Double velY) {
        this.velY = velY;
    }


    public Double getSizeX() {
        return sizeX;
    }

    public void setSizeX(Double sizeX) {
        this.sizeX = sizeX;
    }

    public Double getSizeY() {
        return sizeY;
    }

    public void setSizeY(Double sizeY) {
        this.sizeY = sizeY;
    }

    public void toAnotherCoordinateSystem(Double x, Double y) {
        posX = x - posX;
        posY = y - posY;
        velX = -velX;
        velY = -velY;
    }

    public Rectangle getRect() {
        return new Rectangle(posX.intValue(), posY.intValue(), sizeX.intValue(), sizeY.intValue());
    }
}
