package com.pewpew.pewpew.mongo;

import com.mongodb.MongoClient;
import com.pewpew.pewpew.common.Settings;
import org.jetbrains.annotations.NotNull;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.MappingException;

public final class MongoModule {
    private final Morphia morphia;
    private final MongoClient mongoClient;
    private static MongoModule mongoModule;

    private MongoModule() {
        this.morphia = new Morphia();
        String dbAddress = Settings.DB_ADDRESS;
        int dbPort = Settings.DB_PORT;
        this.mongoClient = new MongoClient(dbAddress, dbPort);
    }

    @NotNull
    public static MongoModule getInstanse() {
        if (mongoModule == null) {
            mongoModule = new MongoModule();
        }
        return mongoModule;
    }

    @NotNull
    public Datastore provideDatastore() {
        if(Settings.MODEL_PACKAGE != null)
        {
            try {
                morphia.mapPackage(Settings.MODEL_PACKAGE);
            }
            catch (MappingException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return morphia.createDatastore(mongoClient, Settings.USERS_COLLECTION);
    }
}
