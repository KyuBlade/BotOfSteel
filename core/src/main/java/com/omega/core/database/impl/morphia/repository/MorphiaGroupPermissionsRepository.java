package com.omega.core.database.impl.morphia.repository;

import com.omega.core.database.entity.permission.GroupPermissions;
import com.omega.core.database.impl.morphia.entity.MorphiaGroupPermissions;
import com.omega.core.database.repository.GroupPermissionsRepository;
import org.mongodb.morphia.Datastore;

public class MorphiaGroupPermissionsRepository extends MorphiaBaseRepository implements GroupPermissionsRepository {

    public MorphiaGroupPermissionsRepository(Datastore datastore) {
        super(datastore);
    }

    @Override
    public GroupPermissions create() {
        return new MorphiaGroupPermissions.GroupPermissionsReferenceProxy(new MorphiaGroupPermissions());
    }

    @Override
    public GroupPermissions create(GroupPermissions group) {
        return new MorphiaGroupPermissions.GroupPermissionsReferenceProxy(new MorphiaGroupPermissions(group));
    }

    @Override
    public GroupPermissions create(String name) {
        return new MorphiaGroupPermissions.GroupPermissionsReferenceProxy(new MorphiaGroupPermissions(name));
    }

    @Override
    public GroupPermissions findById(Object id) {
        return datastore.get(MorphiaGroupPermissions.class, id);
    }

    @Override
    public void save(GroupPermissions entity) {
        datastore.save(entity);
    }

    @Override
    public void delete(GroupPermissions entity) {
        datastore.delete(entity);

        if (entity instanceof MorphiaGroupPermissions.GroupPermissionsReferenceProxy) {
            datastore.delete(((MorphiaGroupPermissions.GroupPermissionsReferenceProxy) entity).getGroup());
        }
    }
}
