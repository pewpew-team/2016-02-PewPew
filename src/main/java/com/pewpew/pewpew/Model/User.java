package com.pewpew.pewpew.Model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by Leman on 19.02.16.
 */
@Entity(value = "users")
public class User {
    @Id
    private ObjectId id;
    private String password;
    private String email;

    public ObjectId getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
