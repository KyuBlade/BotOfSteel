package com.omega.database.entity.permission;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GroupPermissions {

    private Set<String> permissions;
    private String name;

    public GroupPermissions() {
        this.permissions = new HashSet<>();
    }

    public GroupPermissions(String name) {
        this();

        this.name = name;
    }

    public GroupPermissions(GroupPermissions groupPermissions) {
        this();

        this.permissions.addAll(groupPermissions.permissions);
        this.name = groupPermissions.name;
    }

    public String getName() {
        return name;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void addPermission(String permission) {
        this.permissions.add(permission);
    }

    public void addPermissions(String... permissions) {
        this.permissions.addAll(Arrays.asList(permissions));
    }

    public void removePermission(String permission) {
        this.permissions.remove(permission);
    }

    public void removePermissions(String... permissions) {
        this.permissions.removeAll(Arrays.asList(permissions));
    }

    public boolean hasPermission(String permission) {
        return this.permissions.contains(permission);
    }

    public boolean hasPermissions(String... permissions) {
        return this.permissions.containsAll(Arrays.asList(permissions));
    }
}
