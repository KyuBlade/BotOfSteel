package com.omega.core.database.entity.permission;

import com.omega.core.database.DatastoreManagerSingleton;
import com.omega.core.database.repository.GroupPermissionsRepository;
import com.omega.core.database.repository.UserPermissionsRepository;
import com.omega.core.exception.GroupAlreadyExistsException;
import com.omega.core.exception.GroupNotFoundException;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public abstract class GuildPermissions<T extends UserPermissions, S extends GroupPermissions> {

    public enum Fields {
        id, guild, userPermissionsMap, groupPermissionsMap
    }

    public abstract Object getId();

    public abstract IGuild getGuild();

    public abstract Map<IUser, T> getUserPermissionsMap();

    public abstract Map<String, S> getGroupPermissionsMap();

    protected GuildPermissions() {
    }

    public Collection<T> getUserPermissions() {
        Map<IUser, T> userPermissionsMap = getUserPermissionsMap();

        return Collections.unmodifiableCollection(userPermissionsMap.values());
    }

    public Collection<S> getGroupPermissions() {
        Map<String, S> groupPermissionsMap = getGroupPermissionsMap();

        return Collections.unmodifiableCollection(groupPermissionsMap.values());
    }

    public T getPermissionsFor(IUser user) {
        return getUserPermissionsMap().get(user);
    }

    public S getPermissionsFor(String groupName) throws GroupNotFoundException {
        Map<String, S> groupPermissionsMap = getGroupPermissionsMap();

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

    @SuppressWarnings("unchecked")
    private T getOrCreatePermissions(IUser user) {
        UserPermissionsRepository repository = DatastoreManagerSingleton.getInstance()
            .getRepository(UserPermissionsRepository.class);

        Map<IUser, T> userPermissionsMap = getUserPermissionsMap();
        T userPermissions;

        if (userPermissionsMap.containsKey(user)) {
            userPermissions = userPermissionsMap.get(user);
        } else {
            userPermissions = (T) repository.create(user, getDefaultGroup());
            userPermissionsMap.put(user, userPermissions);
        }

        return userPermissions;
    }

    public S getDefaultGroup() {
        return getGroupPermissionsMap().get("default");
    }

    @SuppressWarnings("unchecked")
    public void setUserGroup(IUser user, String groupName) throws GroupNotFoundException {
        UserPermissionsRepository repository = DatastoreManagerSingleton.getInstance()
            .getRepository(UserPermissionsRepository.class);

        Map<IUser, T> userPermissionsMap = getUserPermissionsMap();
        T userPermissions = getPermissionsFor(user);
        S groupPermissions = getPermissionsFor(groupName);

        if (userPermissions == null) {
            userPermissions = (T) repository.create(user, groupPermissions);
            userPermissionsMap.put(user, userPermissions);
        } else {
            userPermissions.setGroup(groupPermissions);
        }
    }

    @SuppressWarnings("unchecked")
    public S addGroup(String groupName) throws GroupAlreadyExistsException {
        Map<String, S> groupPermissionsMap = getGroupPermissionsMap();

        if (!groupPermissionsMap.containsKey(groupName)) {
            GroupPermissionsRepository repository = DatastoreManagerSingleton.getInstance()
                .getRepository(GroupPermissionsRepository.class);

            S group = (S) repository.create(groupName);
            groupPermissionsMap.put(groupName, group);

            return group;
        } else {
            throw new GroupAlreadyExistsException();
        }
    }

    public void addGroup(S groupPermissions) throws GroupAlreadyExistsException {
        Map<String, S> groupPermissionsMap = getGroupPermissionsMap();

        if (!groupPermissionsMap.containsKey(groupPermissions.getName())) {
            groupPermissionsMap.put(groupPermissions.getName(), groupPermissions);
        } else {
            throw new GroupAlreadyExistsException();
        }
    }

    public GroupPermissions removeGroup(String groupName) throws GroupNotFoundException {
        Map<String, S> groupPermissionsMap = getGroupPermissionsMap();

        if (groupPermissionsMap.containsKey(groupName)) {
            getUserPermissions()
                .forEach(user -> {
                    if (user.getGroup().getName().equals(groupName)) {
                        user.setGroup(getDefaultGroup());
                    }
                });

            return groupPermissionsMap.remove(groupName);
        } else {
            throw new GroupNotFoundException();
        }
    }

    public void addGroupPermission(String groupName, String permission) throws GroupNotFoundException {
        Map<String, S> groupPermissionsMap = getGroupPermissionsMap();

        if (groupPermissionsMap.containsKey(groupName)) {
            S groupPermissions = groupPermissionsMap.get(groupName);
            groupPermissions.addPermission(permission);
        } else {
            throw new GroupNotFoundException();
        }
    }

    public void addGroupPermissions(String groupName, String... permissions) throws GroupNotFoundException {
        Map<String, S> groupPermissionsMap = getGroupPermissionsMap();

        if (groupPermissionsMap.containsKey(groupName)) {
            S groupPermissions = groupPermissionsMap.get(groupName);
            groupPermissions.addPermissions(permissions);
        } else {
            throw new GroupNotFoundException();
        }
    }

    public void removeGroupPermission(String groupName, String permission) throws GroupNotFoundException {
        Map<String, S> groupPermissionsMap = getGroupPermissionsMap();

        if (groupPermissionsMap.containsKey(groupName)) {
            S groupPermissions = groupPermissionsMap.get(groupName);
            groupPermissions.removePermission(permission);
        } else {
            throw new GroupNotFoundException();
        }
    }

    public void removeGroupPermissions(String groupName, String... permissions) throws GroupNotFoundException {
        Map<String, S> groupPermissionsMap = getGroupPermissionsMap();

        if (groupPermissionsMap.containsKey(groupName)) {
            S groupPermissions = groupPermissionsMap.get(groupName);
            groupPermissions.removePermissions(permissions);
        } else {
            throw new GroupNotFoundException();
        }
    }

    public boolean hasPermission(IUser user, String permission) {
        Map<IUser, T> userPermissionsMap = getUserPermissionsMap();
        Map<String, S> groupPermissionsMap = getGroupPermissionsMap();

        if (userPermissionsMap.containsKey(user)) {
            T userPermissions = userPermissionsMap.get(user);

            return userPermissions.hasPermission(permission);
        } else {
            return groupPermissionsMap.get("default").hasPermission(permission);
        }
    }
}