package com.pewpew.pewpew.model;

public class PlayerObject {
    public static final double PI = 3.14;
    public static final double START_POSITION_X = 640.0;
    private Double posX;
    private Double velX;
    private Double gunAngle;


    public PlayerObject() {
        this.posX = START_POSITION_X;
        this.velX = 0.0;
    }

    public void toAnotherCoordinateSystem(Double x) {
        posX = x - posX;
        velX = -velX;
    }

    public void translateGunAgnle() {
        gunAngle = PI + gunAngle;
    }
}
