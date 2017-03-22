package com.omega.database.repository;

import com.omega.database.entity.permission.GuildPermissions;
import sx.blah.discord.handle.obj.IGuild;

public interface GuildPermissionsRepository extends Repository<GuildPermissions> {

    GuildPermissions create(IGuild guild);

    GuildPermissions findByGuild(IGuild guild);
}
