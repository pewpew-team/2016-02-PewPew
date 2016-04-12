package com.pewpew.pewpew.model;

import java.util.ArrayList;

public class BulletsCollection {
    private Boolean isReset;
    private ArrayList<BulletObject> bullets;

    public Boolean getReset() {
        return isReset;
    }

    public void setReset(Boolean reset) {
        isReset = reset;
    }

    public ArrayList<BulletObject> getBullets() {
        return bullets;
    }

    public void setBullets(ArrayList<BulletObject> bullets) {
        this.bullets = bullets;
    }
}
