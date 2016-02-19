package com.pewpew.pewpew;

import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.mongo.MongoModule;
import org.junit.Test;
import com.pewpew.pewpew.additional.RandomString;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class TestDataBase {
    private MongoModule mongoModule;

    public TestDataBase() {
        this.mongoModule = MongoModule.getInstanse();
    }

    @Test()
    public void creationTest() {
        User user = new User();
        RandomString randomString = new RandomString(10);
        user.setPassword(randomString.nextString());
        user.setEmail(randomString.nextString());

        List<User> sameUser =  mongoModule.provideDatastore().find(User.class, "email", user.getEmail()).asList();

        assertFalse(sameUser.size() != 0);

        mongoModule.provideDatastore().save(user);
        assertNotNull("Не назначается идентификатор", user.getId());
        assertNotNull("Не создалось поле емеил", user.getEmail());
        assertNotNull("Не создалось поле пароль", user.getPassword());
    }
}
