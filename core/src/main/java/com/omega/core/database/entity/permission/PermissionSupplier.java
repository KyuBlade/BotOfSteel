package com.omega.core.database.entity.permission;

import com.omega.core.module.Supplier;

public interface PermissionSupplier extends Supplier<String> {

    /**
     * @return permissions that will be added to the default group
     */
    String[] supplyDefault();
}
