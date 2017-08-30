package com.omega.core.database.impl.morphia.repository;

import com.omega.core.database.entity.permission.GuildPermissions;
import com.omega.core.database.impl.morphia.entity.MorphiaGuildPermissions;
import com.omega.core.database.repository.GuildPermissionsRepository;
import org.mongodb.morphia.Datastore;
import sx.blah.discord.handle.obj.IGuild;

public class MorphiaGuildPermissionsRepository extends MorphiaBaseRepository implements GuildPermissionsRepository {

    public MorphiaGuildPermissionsRepository(Datastore datastore) {
        super(datastore);
    }

    @Override
    public GuildPermissions create() {
        return new MorphiaGuildPermissions();
    }


    @Override
    public GuildPermissions create(IGuild guild) {
        return new MorphiaGuildPermissions(guild);
    }

    @Override
    public GuildPermissions findById(Object id) {
        return datastore.get(MorphiaGuildPermissions.class, id);
    }

    @Override
    public GuildPermissions findByGuild(IGuild guild) {
        return datastore.createQuery(MorphiaGuildPermissions.class)
            .field(MorphiaGuildPermissions.Fields.guild.name()).equal(guild)
            .get();
    }

    @Override
    public void save(GuildPermissions entity) {
        datastore.save(entity);
    }

    @Override
    public void delete(GuildPermissions entity) {
        datastore.delete(entity);
    }
}
