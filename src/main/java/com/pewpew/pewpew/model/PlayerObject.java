package com.pewpew.pewpew.model;

public class PlayerObject {
    private Double posX;
    private Double velX;
    private Double gunAngle;

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

    public Double getGunAngle() {
        return gunAngle;
    }

    public void setGunAngle(Double gunAngle) {
        this.gunAngle = gunAngle;
    }


    public PlayerObject() {
        this.posX = 640.0;
        this.velX = 0.0;
    }

    public void translateToAnotherCoordinateSystem(Double x) {
        posX = x - posX;
        velX = -velX;
    }

    public void translateGunAgnle() {
        gunAngle = 3.14 - gunAngle;
    }
}
