package com.pewpew.pewpew.Mongo;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 * Created by Leman on 19.02.16.
 */
public class MongoModule {
    private Morphia morphia;
    private MongoClient mongoClient;

    public Datastore provideDatastore() {
        morphia = new Morphia();
        morphia.mapPackage(""); // Указать путь к пакету, который содержит модели
        mongoClient = new MongoClient("127.0.0.1", 27017);
        return morphia.createDatastore(mongoClient, "PewPewDataBase");
    }

}
