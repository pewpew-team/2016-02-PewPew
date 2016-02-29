package com.pewpew.pewpew.common;

import com.pewpew.pewpew.model.User;
import org.jetbrains.annotations.NotNull;


public class Validate {

    @NotNull
    public static boolean userRegister(User user) {
        if (user.getEmail() == null) return false;
        if (user.getLogin() == null) return false;
        if (user.getPassword() == null) return false;
        if (user.getEmail().isEmpty()) return false;
        if (user.getLogin().isEmpty()) return false;
        return !(user.getPassword().isEmpty());
    }

    @NotNull
    public static boolean userAuth(User user) {
        if (user.getLogin() == null) return false;
        if (user.getPassword() == null) return false;
        if (user.getLogin().isEmpty()) return false;
        return !(user.getPassword().isEmpty());
    }

    @NotNull
    public static boolean validateField(String field) {
        if (field == null) return false;
        return field != "";
    }
}
