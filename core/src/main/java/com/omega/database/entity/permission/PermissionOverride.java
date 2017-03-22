package com.omega.database.entity.permission;

public class PermissionOverride {

    public enum OverrideType {
        ADD, REMOVE
    }

    private String permission;
    private OverrideType overrideType;

    public PermissionOverride() {
    }

    public PermissionOverride(String permission, OverrideType overrideType) {
        this.permission = permission;
        this.overrideType = overrideType;
    }

    public String getPermission() {
        return permission;
    }

    public OverrideType getOverrideType() {
        return overrideType;
    }

    public void setOverrideType(OverrideType overrideType) {
        this.overrideType = overrideType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof String) {
            return obj.equals(permission);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return permission.hashCode();
    }
}
