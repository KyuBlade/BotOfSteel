package com.omega.database.entity.permission;

import com.omega.PermissionManager;
import com.omega.exception.GroupAlreadyExistsException;
import com.omega.exception.GroupNotFoundException;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public abstract class GuildPermissions {

    public enum Fields {
        id, guild, userPermissions, groupPermissions
    }

    public abstract Object getId();

    public abstract IGuild getGuild();

    public abstract Map<IUser, UserPermissions> getUserPermissionsMap();

    public abstract Map<String, GroupPermissions> getGroupPermissionsMap();

    protected GuildPermissions() {
    }

    public Collection<UserPermissions> getUserPermissions() {
        Map<IUser, UserPermissions> userPermissionsMap = getUserPermissionsMap();
        return Collections.unmodifiableCollection(userPermissionsMap.values());
    }

    public Collection<GroupPermissions> getGroupPermissions() {
        Map<String, GroupPermissions> groupPermissionsMap = getGroupPermissionsMap();
        return Collections.unmodifiableCollection(groupPermissionsMap.values());
    }

    public UserPermissions getPermissionsFor(IUser user) {
        return getUserPermissionsMap().get(user);
    }

    public GroupPermissions getPermissionsFor(String groupName) throws GroupNotFoundException {
        Map<String, GroupPermissions> groupPermissionsMap = getGroupPermissionsMap();
        if (groupPermissionsMap.containsKey(groupName)) {
            return groupPermissionsMap.get(groupName);
        } else {
            throw new GroupNotFoundException();
        }
    }

    public void addUserPermission(IUser user, String permission) {
        UserPermissions userPermissions = getOrCreatePermissions(user);
        userPermissions.addPermission(permission);
    }

    public void addUserPermissions(IUser user, String... permissions) {
        UserPermissions userPermissions = getOrCreatePermissions(user);
        userPermissions.addPermissions(permissions);
    }

    public void removeUserPermission(IUser user, String permission) {
        UserPermissions userPermissions = getOrCreatePermissions(user);
        userPermissions.removePermission(permission);
    }

    public void removeUserPermissions(IUser user, String... permissions) {
        UserPermissions userPermissions = getOrCreatePermissions(user);
        userPermissions.removePermissions(permissions);
    }

    private UserPermissions getOrCreatePermissions(IUser user) {
        Map<IUser, UserPermissions> userPermissionsMap = getUserPermissionsMap();
        UserPermissions userPermissions;
        if (userPermissionsMap.containsKey(user)) {
            userPermissions = userPermissionsMap.get(user);
        } else {
            userPermissions = new UserPermissions(user, PermissionManager.createDefaultGroup());
            userPermissionsMap.put(user, userPermissions);
        }

        return userPermissions;
    }

    public void setUserGroup(IUser user, String groupName) throws GroupNotFoundException {
        Map<IUser, UserPermissions> userPermissionsMap = getUserPermissionsMap();
        GroupPermissions groupPermissions = getPermissionsFor(groupName);
        UserPermissions userPermissions = getPermissionsFor(user);
        if (userPermissions == null) {
            userPermissions = new UserPermissions(user, groupPermissions);
            userPermissionsMap.put(user, userPermissions);
        } else {
            userPermissions.setGroup(groupPermissions);
        }
    }

    public GroupPermissions addGroup(String groupName) throws GroupAlreadyExistsException {
        Map<String, GroupPermissions> groupPermissionsMap = getGroupPermissionsMap();
        if (!groupPermissionsMap.containsKey(groupName)) {
            GroupPermissions group = new GroupPermissions(groupName);
            groupPermissionsMap.put(groupName, group);

            return group;
        } else {
            throw new GroupAlreadyExistsException();
        }
    }

    public void addGroup(GroupPermissions groupPermissions) throws GroupAlreadyExistsException {
        Map<String, GroupPermissions> groupPermissionsMap = getGroupPermissionsMap();
        if (!groupPermissionsMap.containsKey(groupPermissions.getName())) {
            groupPermissionsMap.put(groupPermissions.getName(), groupPermissions);
        } else {
            throw new GroupAlreadyExistsException();
        }
    }

    public void removeGroup(String groupName) throws GroupNotFoundException {
        Map<String, GroupPermissions> groupPermissionsMap = getGroupPermissionsMap();
        if (groupPermissionsMap.containsKey(groupName)) {
            groupPermissionsMap.remove(groupName);
        } else {
            throw new GroupNotFoundException();
        }
    }

    public void addGroupPermission(String groupName, String permission) throws GroupNotFoundException {
        Map<String, GroupPermissions> groupPermissionsMap = getGroupPermissionsMap();
        if (groupPermissionsMap.containsKey(groupName)) {
            GroupPermissions groupPermissions = groupPermissionsMap.get(groupName);
            groupPermissions.addPermission(permission);
        } else {
            throw new GroupNotFoundException();
        }
    }

    public void addGroupPermissions(String groupName, String... permissions) throws GroupNotFoundException {
        Map<String, GroupPermissions> groupPermissionsMap = getGroupPermissionsMap();
        if (groupPermissionsMap.containsKey(groupName)) {
            GroupPermissions groupPermissions = groupPermissionsMap.get(groupName);
            groupPermissions.addPermissions(permissions);
        } else {
            throw new GroupNotFoundException();
        }
    }

    public void removeGroupPermission(String groupName, String permission) throws GroupNotFoundException {
        Map<String, GroupPermissions> groupPermissionsMap = getGroupPermissionsMap();
        if (groupPermissionsMap.containsKey(groupName)) {
            GroupPermissions groupPermissions = groupPermissionsMap.get(groupName);
            groupPermissions.removePermission(permission);
        } else {
            throw new GroupNotFoundException();
        }
    }

    public void removeGroupPermissions(String groupName, String... permissions) throws GroupNotFoundException {
        Map<String, GroupPermissions> groupPermissionsMap = getGroupPermissionsMap();
        if (groupPermissionsMap.containsKey(groupName)) {
            GroupPermissions groupPermissions = groupPermissionsMap.get(groupName);
            groupPermissions.removePermissions(permissions);
        } else {
            throw new GroupNotFoundException();
        }
    }

    public boolean hasPermission(IUser user, String permission) {
        Map<IUser, UserPermissions> userPermissionsMap = getUserPermissionsMap();
        Map<String, GroupPermissions> groupPermissionsMap = getGroupPermissionsMap();
        if (userPermissionsMap.containsKey(user)) {
            UserPermissions userPermissions = userPermissionsMap.get(user);
            return userPermissions.hasPermission(permission);
        } else {
            groupPermissionsMap.get("default").hasPermission(permission);
        }

        return false;
    }
}