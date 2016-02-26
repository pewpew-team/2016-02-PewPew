package com.pewpew.pewpew.main;

import com.pewpew.pewpew.model.User;

import java.util.HashMap;
import java.util.Map;

public class AccountService {

    private Map<String, User> tokens = new HashMap<>();

    public void addSessions(String token, User user) {

        tokens.put(token, user);
    }

    public User getUserByToken(String token) {

        return tokens.get(token);
    }

    public boolean closeSession(String token) {

        return true;
    }
}
