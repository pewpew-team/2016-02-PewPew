package com.pewpew.pewpew;

import com.pewpew.pewpew.common.Settings;
import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.mongo.MongoModule;
import org.junit.Test;
import com.pewpew.pewpew.additional.RandomString;
import org.mongodb.morphia.Datastore;

import java.util.Random;

import static org.junit.Assert.*;

public class TestDataBase {
    private Datastore datastore;

//    public TestDataBase() {
//        this.datastore = MongoModule.getInstanse().provideDatastore(Settings.USERS_COLLECTION, Settings.MODEL_PACKAGE);
//    }
//
//    @Test()
//    public void creationTest() {
//        User user = new User();
//        RandomString randomString = new RandomString(10);
//        user.setPassword(randomString.nextString());
//        user.setEmail(randomString.nextString());
//        Random rand = new Random();
//        user.setRating(rand.nextInt(100));
//
//        User sameUser =  datastore.find(User.class, "email", user.getEmail()).get();
//        assertNotEquals(user,sameUser);
//
//        datastore.save(user);
//        assertNotNull("Не назначается идентификатор", user.getId());
//        assertNotNull("Не создалось поле емеил", user.getEmail());
//        assertNotNull("Не создалось поле пароль", user.getPassword());
//    }
//
//    @Test
//    public void generateUsers() {
//        for(int i = 0; i < 100; ++i) {
//            User user = new User();
//            RandomString randomString = new RandomString(10);
//            user.setPassword(randomString.nextString());
//            user.setEmail(randomString.nextString());
//            Random rand = new Random();
//            user.setRating(rand.nextInt(100));
//
//            User sameUser = datastore.find(User.class, "email", user.getEmail()).get();
//            assertNotEquals(user, sameUser);
//
//            datastore.save(user);
//        }
//    }
//    @Test
//    public void userExist() {
//
//    }
}
