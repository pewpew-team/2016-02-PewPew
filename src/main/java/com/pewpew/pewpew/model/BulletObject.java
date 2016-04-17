package com.pewpew.pewpew.model;

public class BulletObject {
    private Double posX;
    private Double posY;
    private Double velX;
    private Double velY;
    private Double sizeX;
    private Double sizeY;

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

    public void translateToAnotherCoordinateSystem(Double x, Double y) {
        posX = x - posX;
        posY = posY - y;
        velX = x - velX;
        velY = velY - y;
    }
}
