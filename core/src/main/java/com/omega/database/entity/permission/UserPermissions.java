package com.omega.database.entity.permission;

import sx.blah.discord.handle.obj.IUser;

import java.util.Arrays;
import java.util.Set;

public abstract class UserPermissions {

    public UserPermissions() {
    }

    public abstract IUser getUser();

    public abstract Set<PermissionOverride> getPermissions();

    public abstract GroupPermissions getGroup();

    public abstract void setGroup(GroupPermissions group);

    private PermissionOverride getPermission(String permission) {
        return getPermissions().stream()
            .filter(permissionOverride -> permissionOverride.getPermission().equals(permission))
            .findAny()
            .orElse(null);
    }

    public void addPermission(String permission) {
        PermissionOverride permissionOverride = getPermission(permission);
        if (permissionOverride != null) {
            permissionOverride.setOverrideType(PermissionOverride.OverrideType.ADD);
        } else {
            getPermissions().add(new PermissionOverride(permission, PermissionOverride.OverrideType.ADD));
        }
    }

    public void addPermissions(String... permissions) {
        Arrays.stream(permissions).forEach(this::addPermission);
    }

    public void removePermission(String permission) {
        PermissionOverride permissionOverride = getPermission(permission);
        if (permissionOverride != null) {
            permissionOverride.setOverrideType(PermissionOverride.OverrideType.REMOVE);
        } else {
            getPermissions().add(new PermissionOverride(permission, PermissionOverride.OverrideType.REMOVE));
        }
    }

    public void removePermissions(String... permissions) {
        Arrays.stream(permissions).forEach(this::removePermission);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public boolean hasPermission(String permission) {
        PermissionOverride permissionOverride = getPermissions().stream()
            .filter(permOverride -> permOverride.getPermission().equals(permission))
            .findFirst()
            .orElse(null);

        if (permissionOverride != null) {
            return permissionOverride.getOverrideType().equals(PermissionOverride.OverrideType.ADD);
        }

        return getGroup().hasPermission(permission);
    }

    public boolean hasPermissions(String... permissions) {
        return getPermissions().containsAll(Arrays.asList(permissions));
    }
}
