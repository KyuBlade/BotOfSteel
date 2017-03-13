package com.omega.database.entity.property;

import sx.blah.discord.handle.obj.IChannel;

public class ChannelProperty implements Property<IChannel> {

    private IChannel value;

    public ChannelProperty() {
    }

    public ChannelProperty(IChannel value) {
        this.value = value;
    }

    @Override
    public IChannel getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (value == null) {
            return "null";
        }

        return "Channel(" + value.getName() + ")<" + value.getID() + ">";
    }
}
