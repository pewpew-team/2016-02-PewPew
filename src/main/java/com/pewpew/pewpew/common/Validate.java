package com.pewpew.pewpew.common;

import com.pewpew.pewpew.model.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class Validate {

    public static boolean userRegister(User user) {
        if (user.getEmail() == null) return false;
        if (user.getLogin() == null) return false;
        if (user.getPassword() == null) return false;
        if (user.getEmail().isEmpty()) return false;
        return !user.getLogin().isEmpty() && !(user.getPassword().isEmpty());
    }

    public static boolean userAuth(User user) {
        if (user.getLogin() == null) return false;
        if (user.getPassword() == null) return false;
        return !user.getLogin().isEmpty() && !(user.getPassword().isEmpty());
    }

    public static boolean validateField(@Nullable  String field) {
        return field != null && field != "";
    }
}
