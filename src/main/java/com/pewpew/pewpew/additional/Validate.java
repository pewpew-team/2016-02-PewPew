package com.pewpew.pewpew.additional;

import com.pewpew.pewpew.model.User;
import com.sun.istack.internal.NotNull;


public class Validate {

    @NotNull
    public static boolean user(User user) {
        if (user.getEmail() == null) return false;
        if (user.getEmail().isEmpty()) return false;
        if (user.getPassword().isEmpty()) return false;
        return user.getPassword() != null;
    }
}
