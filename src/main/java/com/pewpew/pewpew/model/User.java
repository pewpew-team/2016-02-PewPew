package com.pewpew.pewpew.model;


import com.pewpew.pewpew.annotations.UserInfo;
import org.jetbrains.annotations.Nullable;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;



@Entity(value = "users")
public class User {
    @SuppressWarnings({"unused", "InstanceVariableNamingConvention"})


    @Id
    @UserInfo
    private String  id;

    @Indexed(unique = true)
    @UserInfo
    private String email;

    @UserInfo
    private String login;

    @Nullable
    private Integer rating;

    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Nullable
//    @XmlTransient
    public Integer getRating() {
        return rating;
    }

    public void setRating(@Nullable Integer rating) {
        this.rating = rating;
    }
}