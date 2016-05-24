package com.pewpew.pewpew.model;

public class Barrier {
    private Double posX;
    private Double posY;
    private final Double sizeX;
    private final Double sizeY;
    private Boolean isRemovable;

    public Barrier() {
        this.sizeX = 30.0;
        this.sizeY = 30.0;
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

    public Boolean isRemovable() {
        return isRemovable;
    }

    public void setRemovable(Boolean removable) {
        isRemovable = removable;
    }

    public Double getSizeY() {
        return sizeY;
    }

    public Double getSizeX() {
        return sizeX;
    }

    public void toAnotherCoordinateSystem(Double x, Double y) {
        posX = x - posX;
        posY = y - posY;
    }

}
