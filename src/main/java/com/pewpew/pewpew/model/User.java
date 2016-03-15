package com.pewpew.pewpew.model;

import com.pewpew.pewpew.annotations.UserInfo;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Email;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;

@Entity(value = "users")
public class User {
    @SuppressWarnings({"unused", "InstanceVariableNamingConvention"})


    @Id
    private ObjectId id;

    private String password;


    @Indexed(unique = true)
    private String email;

    private String login;

    @Nullable
    private Integer rating;


//    public User(String email, String login, String password) {
//        this.email = email;
//        this.login = login;
//        this.password = password;
//    }

    public String getId() {
        return id.toString();
    }

    public void setId(ObjectId id) {
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
    public Integer getRating() {
        return rating;
    }

    public void setRating(@Nullable Integer rating) {
        this.rating = rating;
    }
}
