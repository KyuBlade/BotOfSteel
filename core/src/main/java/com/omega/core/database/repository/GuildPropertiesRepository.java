package com.omega.core.database.repository;

import com.omega.core.database.entity.property.GuildProperties;
import sx.blah.discord.handle.obj.IGuild;

public interface GuildPropertiesRepository extends PropertyRepository<GuildProperties> {

    GuildProperties create(IGuild guild);

    GuildProperties findByGuild(IGuild guild);
}
