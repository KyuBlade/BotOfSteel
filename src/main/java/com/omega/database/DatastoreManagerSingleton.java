package com.omega.database;

import com.mongodb.MongoClient;
import com.omega.database.impl.morphia.MorphiaDatastoreManager;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class DatastoreManagerSingleton {

    private DatastoreManager datastoreManager;

    private DatastoreManagerSingleton() {
    }

    private DatastoreManager createDatastoreManager() {
        return createMorphiaDatastoreManager();
    }

    private MorphiaDatastoreManager createMorphiaDatastoreManager() {
        Morphia morphia = new Morphia();
        Datastore datastore = morphia.createDatastore(new MongoClient("localhost:27017"), "discord_bot");
        return new MorphiaDatastoreManager(morphia, datastore);
    }

    public static DatastoreManager getInstance() {
        DatastoreManagerSingleton singleton = DatastoreManagerSingletonHolder.INSTANCE;
        if (singleton.datastoreManager == null) {
            singleton.datastoreManager = singleton.createDatastoreManager();
        }

        return singleton.datastoreManager;
    }

    private static class DatastoreManagerSingletonHolder {
        private static final DatastoreManagerSingleton INSTANCE = new DatastoreManagerSingleton();
    }
}