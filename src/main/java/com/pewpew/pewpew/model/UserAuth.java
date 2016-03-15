package com.pewpew.pewpew.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.jetbrains.annotations.NotNull;

import javax.validation.constraints.Size;

public class UserAuth {

    @Size(min=1)
    @NotNull
    private String password;

    @Size(min=1)
    @NotNull
    private String login;

    @NotNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    @NotNull
    public String getLogin() {
        return login;
    }

    public void setLogin(@NotNull String login) {
        this.login = login;
    }
}
