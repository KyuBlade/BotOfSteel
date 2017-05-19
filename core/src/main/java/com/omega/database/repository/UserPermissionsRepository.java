package com.omega.database.repository;

import com.omega.database.entity.permission.GroupPermissions;
import com.omega.database.entity.permission.UserPermissions;
import sx.blah.discord.handle.obj.IUser;

public interface UserPermissionsRepository extends Repository<UserPermissions> {

    UserPermissions create(IUser user, GroupPermissions group);
}
