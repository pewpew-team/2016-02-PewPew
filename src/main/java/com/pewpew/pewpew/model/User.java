package com.pewpew.pewpew.model;


import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;



@Entity(value = "users")
public class User {
    @SuppressWarnings({"unused"})


    @Id
    private ObjectId id;

    @Indexed(unique = true)
    private String email;

    @Indexed(unique = true)
    private String login;

    @Nullable
    private Integer rating;

    private String password;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(@Nullable String password) {
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

    @NotNull
    public Integer getRating() {
        if (rating == null)  return 0;
        return rating;
    }

    public void setRating(@Nullable Integer rating) {
        this.rating = rating;
    }
}