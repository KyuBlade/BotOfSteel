package com.omega.core.database.impl.morphia.repository;

import com.omega.core.database.entity.property.BotProperties;
import com.omega.core.database.impl.morphia.entity.MorphiaBotProperties;
import com.omega.core.database.repository.BotPropertiesRepository;
import org.mongodb.morphia.Datastore;

public class MorphiaBotPropertiesRepository extends MorphiaBaseRepository implements BotPropertiesRepository {

    public MorphiaBotPropertiesRepository(Datastore datastore) {
        super(datastore);
    }

    @Override
    public BotProperties create() {
        return new MorphiaBotProperties();
    }

    @Override
    public BotProperties get() {
        return datastore.find(MorphiaBotProperties.class).get();
    }

    @Override
    public BotProperties findById(Object id) {
        return datastore.get(MorphiaBotProperties.class, id);
    }

    @Override
    public void save(BotProperties entity) {
        datastore.save(entity);
    }

    @Override
    public void delete(BotProperties entity) {
        datastore.delete(entity);
    }
}
