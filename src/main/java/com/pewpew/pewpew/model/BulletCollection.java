package com.pewpew.pewpew.model;

import java.util.ArrayList;

public class BulletCollection {
    private Boolean isReset;
    private ArrayList<Bullet> bullets;



    public Boolean getReset() {
        return isReset;
    }

    public void setReset(Boolean reset) {
        isReset = reset;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(ArrayList<Bullet> bullets) {
        this.bullets = bullets;
    }
}
