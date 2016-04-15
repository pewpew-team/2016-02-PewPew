package com.pewpew.pewpew.model;

public class BarriersObject {
    private Double posX;
    private Double posY;
    private Boolean isRemovable;

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
}
