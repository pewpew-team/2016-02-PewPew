package com.pewpew.pewpew.main;

import com.mongodb.MongoClient;
import com.pewpew.pewpew.common.Settings;
import com.pewpew.pewpew.model.User;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountServiceImpl implements AccountService{

    private final Datastore datastore;

    private final Map<String, User> tokens = new HashMap<>();

    public AccountServiceImpl() {
        final Morphia morphia = new Morphia();
        @SuppressWarnings("resource")
        final MongoClient mongoClient = new MongoClient(Settings.DB_ADDRESS, Settings.DB_PORT);
        this.datastore = morphia.createDatastore(mongoClient, Settings.USERS_COLLECTION);
    }

    @Override
    public List<User> getTop() {
        return datastore.find(User.class).retrievedFields(true, "login", "rating").order("-rating").limit(8).asList();
    }

    @Override
    public void addUser(User user) {
        datastore.save(user);
    }

    @Override
    public void addToken(String token, User user) {
        tokens.put(token, user);
    }



    @Override
    @Nullable
    public User getUserByToken(String token) {
        return tokens.get(token);
    }

    @Override
    @Nullable
    public User getUser(String login, String password) {
        return datastore.find(User.class, "login", login).field("password").equal(password).get();
    }

    @Override
    @Nullable
    public User getUserById(String userId) {
        return datastore.get(User.class, new ObjectId(userId));
    }

    @Override
    public Boolean updateUser(String token, @Nullable User editedUser) {
        return tokens.replace(token, editedUser) != null;
    }

    @Override
    @NotNull
    public Boolean userExists(User newUser) {
        User user = datastore.find(User.class, "email", newUser.getEmail()).get();
        return user == null;
    }

    @Override
    public void deleteUser(User user) {
        tokens.values().remove(user);
    }

    @Override
    public Boolean closeToken(String token) {
        return tokens.remove(token) == null;
    }


    @Override
    public void delete(User user) {
        datastore.delete(user);
    }
}
