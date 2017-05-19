package com.omega.database.entity.property;

import sx.blah.discord.handle.obj.IUser;

public class UserProperty implements Property<IUser> {

    private IUser value;

    public UserProperty() {
    }

    public UserProperty(IUser value) {
        this.value = value;
    }

    @Override
    public IUser getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (value == null) {
            return null;
        }

        return "User(" + value.getName() + ")<" + value.getLongID() + ">";
    }
}
