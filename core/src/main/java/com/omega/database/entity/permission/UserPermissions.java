package com.omega.database.entity.permission;

import com.omega.PermissionManager;
import sx.blah.discord.handle.obj.IUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UserPermissions {

    private Set<PermissionOverride> permissions;
    private IUser user;
    private GroupPermissions group;

    public UserPermissions() {
        this.permissions = new HashSet<>();
    }

    public UserPermissions(IUser user, GroupPermissions group) {
        this();

        this.user = user;
        if (group != null) {
            this.group = group;
        } else {
            this.group = PermissionManager.createDefaultGroup();
        }
    }

    public IUser getUser() {
        return user;
    }

    public GroupPermissions getGroup() {
        return group;
    }

    public void setGroup(GroupPermissions group) {
        this.group = group;
    }

    public Set<PermissionOverride> getPermissions() {
        return Collections.unmodifiableSet(permissions);
    }

    private PermissionOverride getPermission(String permission) {
        return permissions.stream()
            .filter(permissionOverride -> permissionOverride.getPermission().equals(permission))
            .findAny()
            .orElse(null);
    }

    public void addPermission(String permission) {
        PermissionOverride permissionOverride = getPermission(permission);
        if (permissionOverride != null) {
            permissionOverride.setOverrideType(PermissionOverride.OverrideType.ADD);
        } else {
            this.permissions.add(new PermissionOverride(permission, PermissionOverride.OverrideType.ADD));
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
            this.permissions.add(new PermissionOverride(permission, PermissionOverride.OverrideType.REMOVE));
        }
    }

    public void removePermissions(String... permissions) {
        Arrays.stream(permissions).forEach(this::removePermission);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public boolean hasPermission(String permission) {
        PermissionOverride permissionOverride = permissions.stream().filter(permOverride ->
            permOverride.getPermission().equals(permission)
        ).findFirst().orElse(null);
        if (permissionOverride != null) {
            return permissionOverride.getOverrideType().equals(PermissionOverride.OverrideType.ADD);
        }

        return group.hasPermission(permission);
    }

    public boolean hasPermissions(String... permissions) {
        return this.permissions.containsAll(Arrays.asList(permissions));
    }
}
