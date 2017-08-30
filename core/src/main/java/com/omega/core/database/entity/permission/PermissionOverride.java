package com.omega.core.database.entity.permission;

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
        return obj != null && obj instanceof String && obj.equals(permission);

    }

    @Override
    public int hashCode() {
        return permission.hashCode();
    }
}
