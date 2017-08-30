package com.omega.core.database.repository;

import com.omega.core.database.entity.permission.GroupPermissions;

public interface GroupPermissionsRepository extends Repository<GroupPermissions> {

    GroupPermissions create(GroupPermissions group);

    GroupPermissions create(String name);
}
