package com.omega.database.repository;

import com.omega.database.entity.permission.PrivateChannelPermissions;

public interface PrivateChannelPermissionsRepository extends Repository<PrivateChannelPermissions> {

    PrivateChannelPermissions find();
}
