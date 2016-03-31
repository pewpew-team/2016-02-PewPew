package com.pewpew.pewpew.model;

import java.util.List;

@SuppressWarnings("unused")
public class Users {
    private final List<User> scores;
    public Users(List<User> scores) {
        this.scores = scores;
    }
    public List<User> getScores() {
        return scores;
    }
}
