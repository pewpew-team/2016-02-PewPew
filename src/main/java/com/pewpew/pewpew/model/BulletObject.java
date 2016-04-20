package com.pewpew.pewpew.model;

public class BulletObject {

    private Integer bulletId = 0;
    private Double posX;
    private Double posY;
    private Double velX;
    private Double velY;
    private Double sizeX;
    private Double sizeY;

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

    public void tooAnotherCoordinateSystem(Double x, Double y) {
        posX = x - posX;
        posY = y - posY;
        velX = -velX;
        velY = -velY;
    }
}
