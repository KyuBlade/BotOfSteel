package com.omega.database.repository;

import com.omega.database.entity.permission.GroupPermissions;

public interface GroupPermissionsRepository extends Repository<GroupPermissions> {

    GroupPermissions create(GroupPermissions group);

    GroupPermissions create(String name);
}
