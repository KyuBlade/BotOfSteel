package com.omega.database.morphium.mapper;

import com.omega.BotManager;
import de.caluga.morphium.TypeMapper;
import sx.blah.discord.handle.obj.IRole;

public class RoleTypeMapper implements TypeMapper<IRole> {

    @Override
    public Object marshall(IRole o) {
        return o.getID();
    }

    @Override
    public IRole unmarshall(Object d) {
        if (d == null) {
            return null;
        }

        return BotManager.getInstance().getClient().getRoleByID((String) d);
    }
}
