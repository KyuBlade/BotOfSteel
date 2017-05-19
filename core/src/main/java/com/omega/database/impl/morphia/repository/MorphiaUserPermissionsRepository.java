package com.omega.database.impl.morphia.repository;

import com.omega.database.entity.permission.GroupPermissions;
import com.omega.database.entity.permission.UserPermissions;
import com.omega.database.impl.morphia.entity.MorphiaGroupPermissions;
import com.omega.database.impl.morphia.entity.MorphiaUserPermissions;
import com.omega.database.repository.UserPermissionsRepository;
import org.mongodb.morphia.Datastore;
import sx.blah.discord.handle.obj.IUser;

public class MorphiaUserPermissionsRepository extends MorphiaBaseRepository implements UserPermissionsRepository {

    public MorphiaUserPermissionsRepository(Datastore datastore) {
        super(datastore);
    }

    @Override
    public UserPermissions create() {
        return new MorphiaUserPermissions();
    }

    @Override
    public UserPermissions create(IUser user, GroupPermissions group) {
        return new MorphiaUserPermissions(user, (MorphiaGroupPermissions) group);
    }

    @Override
    public UserPermissions findById(Object id) {
        return null;
    }

    @Override
    public void save(UserPermissions entity) {
        datastore.save(entity);
    }

    @Override
    public void delete(UserPermissions entity) {
        datastore.delete(entity);
    }
}
