package com.pewpew.pewpew.model;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class AccountService {

    private final Map<String, User> tokens = new HashMap<>();

    public void addToken(String token, User user) {
        tokens.put(token, user);
    }

    @Nullable
    public User getUserByToken(String token) {
        return tokens.get(token);
    }

    public boolean updateUser(String token, @Nullable User editedUser) {
        return tokens.replace(token, editedUser) != null;
    }

    public void deleteUser(User user) {
        tokens.values().remove(user);
    }

    public boolean closeToken(String token) {
        return tokens.remove(token) == null;
    }
}
