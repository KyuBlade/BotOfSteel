package com.omega.database.impl.morphia.repository;

import com.omega.database.entity.permission.PrivateChannelPermissions;
import com.omega.database.impl.morphia.entity.MorphiaPrivateChannelsPermissions;
import com.omega.database.repository.PrivateChannelPermissionsRepository;
import org.mongodb.morphia.Datastore;

public class MorphiaPrivateChannelPermissionsRepository extends MorphiaBaseRepository implements PrivateChannelPermissionsRepository {

    public MorphiaPrivateChannelPermissionsRepository(Datastore datastore) {
        super(datastore);
    }

    @Override
    public PrivateChannelPermissions find() {
        return datastore.find(MorphiaPrivateChannelsPermissions.class).get();
    }

    @Override
    public PrivateChannelPermissions create() {
        return new MorphiaPrivateChannelsPermissions();
    }

    @Override
    public PrivateChannelPermissions findById(Object id) {
        return datastore.get(MorphiaPrivateChannelsPermissions.class, id);
    }

    @Override
    public void save(PrivateChannelPermissions entity) {
        datastore.save(entity);
    }

    @Override
    public void delete(PrivateChannelPermissions entity) {
        datastore.delete(entity);
    }
}
