package com.pewpew.pewpew.model;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pewpew.pewpew.annotations.UserInfo;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.Nullable;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;




//@XmlRootElement
@Entity(value = "users")
public class User {
    @SuppressWarnings({"unused", "InstanceVariableNamingConvention"})


    @Id
    @UserInfo
    private String  id;

    private String password;


    @Indexed(unique = true)
    @UserInfo
    private String email;

    @UserInfo
    private String login;

    @Nullable
    private Integer rating;


//    public User(String email, String login, String password) {
//        this.email = email;
//        this.login = login;
//        this.password = password;
//    }

//    @XmlElement(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    @XmlTransient
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    @XmlTransient
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    @XmlTransient
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


