package com.pewpew.pewpew.mongo;
import com.pewpew.pewpew.common.Settings;
import com.pewpew.pewpew.model.User;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mongodb.morphia.Datastore;

public class MongoManager {
    private static MongoModule mongoModule = MongoModule.getInstanse();
    private static Datastore ds = mongoModule.provideDatastore(Settings.USERS_COLLECTION, Settings.MODEL_PACKAGE);

    @Nullable
    public static User getUser(String email) {
        return ds.find(User.class, "email", email).get();
    }

    public static User getUser(String email, String password) {
        return ds.find(User.class, "email", email).field("password").equal(password).get();
    }

    public static User getUser(ObjectId userId) {
        return ds.get(User.class, userId);
    }

    @NotNull
    public static Boolean userExist(User newUser) {
        User user = getUser(newUser.getEmail());
        return user == null;
    }
}
