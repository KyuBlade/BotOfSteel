package com.omega.core.database.repository;

import com.omega.core.database.entity.permission.GroupPermissions;
import com.omega.core.database.entity.permission.UserPermissions;
import sx.blah.discord.handle.obj.IUser;

public interface UserPermissionsRepository extends Repository<UserPermissions> {

    UserPermissions create(IUser user, GroupPermissions group);
}
