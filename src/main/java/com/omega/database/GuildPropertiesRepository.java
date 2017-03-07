package com.omega.database;

import com.omega.database.entity.GuildProperties;
import sx.blah.discord.handle.obj.IGuild;

public interface GuildPropertiesRepository extends Repository<GuildProperties> {

    GuildProperties create(IGuild guild);

    GuildProperties findByGuild(IGuild guild);
}
