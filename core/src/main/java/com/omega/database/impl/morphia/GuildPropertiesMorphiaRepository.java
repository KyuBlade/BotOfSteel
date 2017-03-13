package com.omega.database.impl.morphia;

import com.omega.database.GuildPropertiesRepository;
import com.omega.database.entity.GuildProperties;
import com.omega.database.impl.morphia.entity.MorphiaGuildProperties;
import org.mongodb.morphia.Datastore;
import sx.blah.discord.handle.obj.IGuild;

public class GuildPropertiesMorphiaRepository extends MorphiaBaseRepository implements GuildPropertiesRepository {

    public GuildPropertiesMorphiaRepository(Datastore datastore) {
        super(datastore);
    }

    @Override
    public GuildProperties create() throws IllegalAccessException, InstantiationException {
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
    public void save(GuildProperties entity) {
        datastore.save(entity);
    }

    @Override
    public void delete(GuildProperties entity) {
        datastore.delete(entity);
    }

    @Override
    public MorphiaGuildProperties findByGuild(IGuild guild) {
        return datastore.createQuery(MorphiaGuildProperties.class)
            .field(MorphiaGuildProperties.Fields.guild.name()).equal(guild)
            .get();
    }
}
