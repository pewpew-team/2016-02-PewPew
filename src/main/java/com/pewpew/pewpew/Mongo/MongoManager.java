package com.pewpew.pewpew.mongo;
import com.pewpew.pewpew.model.User;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;

public class MongoManager {
    private static MongoModule mongoModule = MongoModule.getInstanse();

//    @Nullable
    public static User getUser(String email) {
        return mongoModule.provideDatastore().find(
                User.class, "email", email).get();
    }
    public static User getUser(String email, String password) {
        return mongoModule.provideDatastore().find(
                User.class, "email", email).field("password").equal(password).get();
    }

//    @NotNull
    public static Boolean userExist(User user) {
        User oldUser = getUser(user.getEmail());
        return oldUser == null;
    }
}
