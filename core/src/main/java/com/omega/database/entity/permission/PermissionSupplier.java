package com.omega.database.entity.permission;

import com.omega.module.Supplier;

public interface PermissionSupplier extends Supplier<String> {

    /**
     * @return permissions that will be added to the default group
     */
    String[] supplyDefault();
}
