package com.omega.database.impl.morphia.repository;

import com.omega.database.repository.GuildPropertiesRepository;
import com.omega.database.entity.property.GuildProperties;
import com.omega.database.impl.morphia.entity.MorphiaGuildProperties;
import org.mongodb.morphia.Datastore;
import sx.blah.discord.handle.obj.IGuild;

public class MorphiaGuildPropertiesRepository extends MorphiaBaseRepository implements GuildPropertiesRepository {

    public MorphiaGuildPropertiesRepository(Datastore datastore) {
        super(datastore);
    }

    @Override
    public GuildProperties create() {
        return new MorphiaGuildProperties();
    }

    @Override
    public GuildProperties create(IGuild guild) {
        return new MorphiaGuildProperties(guild);
    }

    @Override
    public GuildProperties findById(Object id) {
        return datastore.get(MorphiaGuildProperties.class, id);
    }

    @Override
    public MorphiaGuildProperties findByGuild(IGuild guild) {
        return datastore.createQuery(MorphiaGuildProperties.class)
            .field(MorphiaGuildProperties.Fields.guild.name()).equal(guild)
            .get();
    }

    @Override
    public void save(GuildProperties entity) {
        datastore.save(entity);
    }

    @Override
    public void delete(GuildProperties entity) {
        datastore.delete(entity);
    }
}
