package com.pewpew.pewpew.main;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.pewpew.pewpew.model.User;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class AccountServiceImpl implements AccountService {

    private final Datastore datastore;

    private final Map<String, User> tokens = new HashMap<>();

    public AccountServiceImpl() throws MongoException {
        final Morphia morphia = new Morphia();
        final Properties property = new Properties();
        try(FileInputStream fileInputStream =
                    new FileInputStream("src/main/java/com/pewpew/pewpew/resources/database.properties")) {
            property.load(fileInputStream);
        } catch (IOException e) {
            System.out.println("Can't start mongo");
        }
        final MongoClient mongoClient = new MongoClient(property.getProperty("db.adress"),
                    Integer.parseInt(property.getProperty("db.port")));
        this.datastore = morphia.createDatastore(mongoClient, property.getProperty("db.collection"));
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
    public void updateUser(String token, @Nullable User editedUser) {
        tokens.replace(token, editedUser);
    }

    @Override
    @NotNull
    public Boolean userExists(User newUser) {
        final User user = datastore.find(User.class, "email", newUser.getEmail()).get();
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
