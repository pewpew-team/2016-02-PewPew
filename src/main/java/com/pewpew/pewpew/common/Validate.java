package com.pewpew.pewpew.common;

import com.pewpew.pewpew.model.User;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public class Validate {

    public static boolean userRegister(User user) {
        //noinspection OverlyComplexBooleanExpression
        return user.getEmail() != null &&
                user.getLogin() != null &&
                user.getPassword() != null &&
                !user.getEmail().isEmpty() &&
                !user.getLogin().isEmpty() &&
                !(user.getPassword().isEmpty());
    }

    public static boolean userAuth(User user) {
        //noinspection OverlyComplexBooleanExpression
        return user.getLogin() != null &&
                user.getPassword() != null &&
                !user.getLogin().isEmpty() &&
                !(user.getPassword().isEmpty());
    }

    public static boolean validateField(@Nullable  String field) {
        return field != null && !Objects.equals(field, "");
    }
}
