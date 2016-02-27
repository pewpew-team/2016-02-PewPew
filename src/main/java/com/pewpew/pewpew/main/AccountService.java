package com.pewpew.pewpew.main;

import com.pewpew.pewpew.model.User;

import java.util.HashMap;
import java.util.Map;

public class AccountService {

    private Map<String, User> tokens = new HashMap<>();

    public boolean addToken(String token, User user) {
        return tokens.put(token, user) != null;
    }

    public User getUserByToken(String token) {
        return tokens.get(token);
    }

    public boolean updateUser(String token, User editedUser) {
        return tokens.replace(token, editedUser) != null;
    }

    public boolean deleteUser(User user) {
        return tokens.values().remove(user);
    }

    public boolean closeToken(String token) {
        return tokens.remove(token) != null;
    }
}
