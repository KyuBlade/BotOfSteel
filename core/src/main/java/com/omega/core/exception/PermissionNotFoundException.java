package com.omega.core.exception;

public class PermissionNotFoundException extends Exception {

    private final String permission;

    public PermissionNotFoundException() {
        this.permission = null;
    }

    public PermissionNotFoundException(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
