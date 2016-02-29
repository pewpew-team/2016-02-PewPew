package com.pewpew.pewpew.mongo;

import com.mongodb.MongoClient;
import com.pewpew.pewpew.common.Settings;
import org.jetbrains.annotations.NotNull;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.MappingException;

public class MongoModule {
    private String dbAddress = Settings.DB_ADDRESS;
    private int dbPort = Settings.DB_PORT;
    private Morphia morphia;
    private MongoClient mongoClient;
    private static MongoModule mongoModule;

    private MongoModule() {
        this.morphia = new Morphia();
        this.mongoClient = new MongoClient(this.dbAddress, this.dbPort);
    }

    @NotNull
    public static MongoModule getInstanse() {
        if (mongoModule == null) {
            mongoModule = new MongoModule();
        }
        return mongoModule;
    }

    @NotNull
    public Datastore provideDatastore(String dbName, String mapPackage) {
        if(mapPackage != null)
        {
            try {
                morphia.mapPackage(mapPackage);
            }
            catch (MappingException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return morphia.createDatastore(mongoClient, dbName);
    }
}
