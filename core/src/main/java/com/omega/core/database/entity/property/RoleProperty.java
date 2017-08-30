package com.omega.core.database.entity.property;

import sx.blah.discord.handle.obj.IRole;

public class RoleProperty implements Property<IRole> {

    private IRole value;

    public RoleProperty() {
    }

    public RoleProperty(IRole value) {
        this.value = value;
    }

    @Override
    public IRole getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (value == null) {
            return null;
        }

        return "Role(" + value.getName() + ")<" + value.getLongID() + ">";
    }
}
