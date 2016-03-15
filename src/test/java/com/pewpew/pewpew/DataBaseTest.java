package com.pewpew.pewpew;

import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.mongo.MongoModule;
import org.junit.Test;
import com.pewpew.pewpew.common.RandomString;
import org.mongodb.morphia.Datastore;

import java.util.Random;

import static org.junit.Assert.*;

public class DataBaseTest {
    private final Datastore datastore;

    public DataBaseTest() {
        this.datastore = MongoModule.getInstanse().provideDatastore();
    }

    @Test()
    public void creationTest() {
        RandomString randomString = new RandomString();
        User user = new User();
        user.setEmail(randomString.nextString());
        user.setLogin(randomString.nextString());
        user.setPassword(randomString.nextString());
//        User user = new User(randomString.nextString(), randomString.nextString(), randomString.nextString());
        Random rand = new Random();
        user.setRating(rand.nextInt(100));

        User sameUser =  datastore.find(User.class, "email", user.getEmail()).get();
        assertNotEquals(user,sameUser);

        datastore.save(user);
        assertNotNull("Не назначается идентификатор", user.getId());
        assertNotNull("Не создалось поле емеил", user.getEmail());
        assertNotNull("Не создалось поле пароль", user.getPassword());
    }

    @Test
    public void generateUsers() {
        for(int i = 0; i < 100; ++i) {
            RandomString randomString = new RandomString();
            User user = new User();
            user.setEmail(randomString.nextString());
            user.setLogin(randomString.nextString());
            user.setPassword(randomString.nextString());
            Random rand = new Random();
            user.setRating(rand.nextInt(100));

            User sameUser = datastore.find(User.class, "email", user.getEmail()).get();
            assertNotEquals(user, sameUser);

            datastore.save(user);
        }
    }
}
