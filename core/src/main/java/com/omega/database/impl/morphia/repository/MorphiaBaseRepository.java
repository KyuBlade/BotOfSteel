package com.omega.database.impl.morphia.repository;

import org.mongodb.morphia.Datastore;

public class MorphiaBaseRepository {

    protected final Datastore datastore;

    public MorphiaBaseRepository(Datastore datastore) {
        this.datastore = datastore;
    }
}
