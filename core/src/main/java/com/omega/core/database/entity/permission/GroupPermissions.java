package com.omega.core.database.entity.permission;


import java.util.Arrays;
import java.util.Set;

public abstract class GroupPermissions {

    public abstract String getName();

    public abstract Set<String> getPermissions();

    public void addPermission(String permission) {
        getPermissions().add(permission);
    }

    public void addPermissions(String... permissions) {
        getPermissions().addAll(Arrays.asList(permissions));
    }

    public void removePermission(String permission) {
        getPermissions().remove(permission);
    }

    public void removePermissions(String... permissions) {
        getPermissions().removeAll(Arrays.asList(permissions));
    }

    public boolean hasPermission(String permission) {
        return getPermissions().contains(permission);
    }

    public boolean hasPermissions(String... permissions) {
        return getPermissions().containsAll(Arrays.asList(permissions));
    }
}
