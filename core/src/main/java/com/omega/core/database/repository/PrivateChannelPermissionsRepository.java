package com.omega.core.database.repository;

import com.omega.core.database.entity.permission.PrivateChannelPermissions;

public interface PrivateChannelPermissionsRepository extends Repository<PrivateChannelPermissions> {

    PrivateChannelPermissions find();
}
