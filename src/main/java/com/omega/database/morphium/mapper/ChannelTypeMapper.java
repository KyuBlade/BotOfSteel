package com.omega.database.morphium.mapper;

import com.omega.BotManager;
import de.caluga.morphium.TypeMapper;
import sx.blah.discord.handle.obj.IChannel;

public class ChannelTypeMapper implements TypeMapper<IChannel> {

    @Override
    public Object marshall(IChannel o) {
        return o.getID();
    }

    @Override
    public IChannel unmarshall(Object d) {
        if (d == null) {
            return null;
        }

        return BotManager.getInstance().getClient().getChannelByID((String) d);
    }
}
