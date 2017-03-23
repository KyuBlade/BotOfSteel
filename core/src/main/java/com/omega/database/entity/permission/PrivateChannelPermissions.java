package com.omega.database.entity.permission;

import com.omega.PermissionManager;
import sx.blah.discord.handle.obj.IUser;

import java.util.Map;

public abstract class PrivateChannelPermissions {

    private transient final GroupPermissions DEFAULT_GROUP;

    public PrivateChannelPermissions() {
        this.DEFAULT_GROUP = PermissionManager.createDefaultGroup();
    }

    public abstract Map<IUser, UserPermissions> getUserPermissionsMap();

    public boolean hasPermission(IUser user, String permission) {
        Map<IUser, UserPermissions> userPermissionsMap = getUserPermissionsMap();
        UserPermissions userPermissions = userPermissionsMap.get(user);
        if (userPermissions != null) {
            return userPermissions.hasPermission(permission);
        } else {
            return DEFAULT_GROUP.hasPermission(permission);
        }
    }

    public void addUserPermission(IUser user, String permission) {
        Map<IUser, UserPermissions> userPermissionsMap = getUserPermissionsMap();
        UserPermissions userPermissions;
        if (!userPermissionsMap.containsKey(user)) {
            userPermissions = new UserPermissions(user, DEFAULT_GROUP);
            userPermissionsMap.put(user, userPermissions);
        } else {
            userPermissions = userPermissionsMap.get(user);
        }

        userPermissions.addPermission(permission);
    }

    public void addUserPermissions(IUser user, String... permissions) {
        Map<IUser, UserPermissions> userPermissionsMap = getUserPermissionsMap();
        UserPermissions userPermissions;
        if (!userPermissionsMap.containsKey(user)) {
            userPermissions = new UserPermissions(user, DEFAULT_GROUP);
            userPermissionsMap.put(user, userPermissions);
        } else {
            userPermissions = userPermissionsMap.get(user);
        }

        userPermissions.addPermissions(permissions);
    }

    public void addGroupPermission(String permission) {
        DEFAULT_GROUP.addPermission(permission);
    }

    public void removeGroupPermission(String permission) {
        DEFAULT_GROUP.removePermission(permission);
    }

    public void removeUserPermission(IUser user, String permission) {
        Map<IUser, UserPermissions> userPermissionsMap = getUserPermissionsMap();
        UserPermissions userPermissions;
        if (!userPermissionsMap.containsKey(user)) {
            userPermissions = new UserPermissions(user, DEFAULT_GROUP);
            userPermissionsMap.put(user, userPermissions);
        } else {
            userPermissions = userPermissionsMap.get(user);
        }

        userPermissions.removePermission(permission);
    }

    public void removeUserPermissions(IUser user, String... permissions) {
        Map<IUser, UserPermissions> userPermissionsMap = getUserPermissionsMap();
        UserPermissions userPermissions;
        if (!userPermissionsMap.containsKey(user)) {
            userPermissions = new UserPermissions(user, DEFAULT_GROUP);
            userPermissionsMap.put(user, userPermissions);
        } else {
            userPermissions = userPermissionsMap.get(user);
        }

        userPermissions.removePermissions(permissions);
    }

    public UserPermissions getPermissionsFor(IUser user) {
        Map<IUser, UserPermissions> userPermissionsMap = getUserPermissionsMap();
        return userPermissionsMap.get(user);
    }
}
