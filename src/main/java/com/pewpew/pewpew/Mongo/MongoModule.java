package com.pewpew.pewpew.mongo;

import com.mongodb.MongoClient;
import com.sun.istack.internal.NotNull;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class MongoModule {
    private Morphia morphia;
    private MongoClient mongoClient;
    private static MongoModule mongoModule;

    public MongoModule(Morphia morphia, MongoClient mongoClient) {
        this.morphia = morphia;
        this.mongoClient = mongoClient;
    }

    @NotNull
    public static MongoModule getInstanse() {
        if (mongoModule != null) {

            return mongoModule;
        }
        Morphia morphia = new Morphia();
        morphia.mapPackage("com.pewpew.pewpew.model");
        MongoClient mongoClient = new MongoClient("127.0.0.1", 27017);
        mongoModule = new MongoModule(morphia, mongoClient);
        return mongoModule;
    }

    @NotNull
    public Datastore provideDatastore() {
        return morphia.createDatastore(mongoClient, "PewPewDataBase");
    }
}
