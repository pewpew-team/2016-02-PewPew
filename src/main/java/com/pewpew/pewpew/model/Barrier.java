package com.pewpew.pewpew.model;

public class Barrier {
    private Double posX;
    private Double posY;
    private Double sizeX;
    private Double sizeY;
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

    public Boolean getRemovable() {
        return isRemovable;
    }

    public void setRemovable(Boolean removable) {
        isRemovable = removable;
    }

    public Double getSizeY() {
        return sizeY;
    }

    public void setSizeY(Double sizeY) {
        this.sizeY = sizeY;
    }

    public Double getSizeX() {
        return sizeX;
    }

    public void setSizeX(Double sizeX) {
        this.sizeX = sizeX;
    }
}
