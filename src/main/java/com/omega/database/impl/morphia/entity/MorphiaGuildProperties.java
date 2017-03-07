package com.omega.database.impl.morphia.entity;

import com.omega.database.entity.GuildProperties;
import com.omega.database.entity.property.Property;
import com.omega.guild.GuildContext;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import sx.blah.discord.handle.obj.IGuild;

import java.util.HashMap;
import java.util.Map;

@Entity(value = "guild_properties", noClassnameStored = true)
public class MorphiaGuildProperties extends GuildProperties {

    @Id
    private ObjectId id;

    @Indexed(options = @IndexOptions(unique = true))
    @Embedded
    private IGuild guild;

    @Transient
    private GuildContext guildContext;

    @Embedded
    private Map<String, Property> properties;

    public MorphiaGuildProperties() {
        this.properties = new HashMap<>();
    }

    public MorphiaGuildProperties(IGuild guild) {
        this();

        this.guild = guild;
    }

    @Override
    public ObjectId getId() {
        return id;
    }

    @Override
    public IGuild getGuild() {
        return guild;
    }

    @Override
    public GuildContext getGuildContext() {
        return guildContext;
    }

    @Override
    public void setGuildContext(GuildContext guildContext) {
        this.guildContext = guildContext;
    }

    @Override
    public Map<String, Property> getProperties() {
        return properties;
    }
}
