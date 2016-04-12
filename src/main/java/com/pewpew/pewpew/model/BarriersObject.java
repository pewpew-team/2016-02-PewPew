package com.pewpew.pewpew.model;

public class BarriersObject {
    private Integer posX;
    private Integer posY;
    private Boolean isRemovable;

    public Integer getPosX() {
        return posX;
    }

    public void setPosX(Integer posX) {
        this.posX = posX;
    }

    public Integer getPosY() {
        return posY;
    }

    public void setPosY(Integer posY) {
        this.posY = posY;
    }

    public Boolean getRemovable() {
        return isRemovable;
    }

    public void setRemovable(Boolean removable) {
        isRemovable = removable;
    }
}
