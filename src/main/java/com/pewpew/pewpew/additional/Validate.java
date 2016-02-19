package com.pewpew.pewpew.additional;

import com.pewpew.pewpew.model.User;

/**
 * Created by Leman on 19.02.16.
 */
public class Validate {
    public static boolean user(User user) {
        if (user.getEmail() == null) return false;
        if (user.getEmail().isEmpty()) return false;
        if (user.getPassword().isEmpty()) return false;
        return user.getPassword() != null;
    }
}
