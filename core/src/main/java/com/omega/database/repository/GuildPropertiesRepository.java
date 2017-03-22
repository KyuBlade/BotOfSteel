package com.omega.database.repository;

import com.omega.database.entity.property.GuildProperties;
import sx.blah.discord.handle.obj.IGuild;

public interface GuildPropertiesRepository extends Repository<GuildProperties> {

    GuildProperties create(IGuild guild);

    GuildProperties findByGuild(IGuild guild);
}
