package com.omega.database.morphium.mapper;

import com.omega.BotManager;
import de.caluga.morphium.TypeMapper;
import sx.blah.discord.handle.obj.IUser;

public class UserTypeMapper implements TypeMapper<IUser> {

    @Override
    public Object marshall(IUser o) {
        return o.getID();
    }

    @Override
    public IUser unmarshall(Object d) {
        if (d == null) {
            return null;
        }

        return BotManager.getInstance().getClient().getUserByID((String) d);
    }
}
