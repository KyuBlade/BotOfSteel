package com.omega.database.entity.property;

import com.omega.database.repository.GuildPropertiesRepository;
import com.omega.guild.GuildContext;
import com.omega.guild.property.PropertyChangeTask;
import com.omega.guild.property.PropertyDefinition;
import com.omega.guild.property.PropertySupplier;
import sx.blah.discord.handle.obj.IGuild;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class GuildProperties extends Properties {

    public enum Fields {
        id, guild, properties
    }

    private static final Map<String, PropertyDefinition> PROPERTY_DEFINITIONS = new HashMap<>();

    public GuildProperties() {
        super(GuildPropertiesRepository.class);
    }

    public abstract Object getId();

    public abstract IGuild getGuild();

    protected abstract GuildContext getGuildContext();

    protected abstract void setGuildContext(GuildContext guildContext);

    @SuppressWarnings("unchecked")
    public void initLoad(GuildContext guildContext) {
        setGuildContext(guildContext);

        initLoad();
    }

    public void initDefault(GuildContext guildContext) {
        setGuildContext(guildContext);

        initDefault();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void executePropertyChangeTask(PropertyChangeTask task, Property property, boolean init) {
        task.execute(getGuildContext(), property, init);
    }

    @Override
    public Map<String, PropertyDefinition> getPropertyDefinitions() {
        return PROPERTY_DEFINITIONS;
    }

    /**
     * Add available property definitions.
     *
     * @param supplier property definition supplier with properties to add
     */
    public static void supply(PropertySupplier supplier) {
        PropertyDefinition[] propertyDefinitions = supplier.supply();
        Arrays.stream(propertyDefinitions).forEach(propertyDefinition ->
            PROPERTY_DEFINITIONS.put(propertyDefinition.getPropertyKey(), propertyDefinition)
        );
    }

    /**
     * Remove property previously added by a supplier.
     *
     * @param supplier property definition supplier with properties to remove
     */
    public static void unsupply(PropertySupplier supplier) {
        PropertyDefinition[] propertyDefinitions = supplier.supply();
        Arrays.stream(propertyDefinitions).forEach(propertyDefinition ->
            PROPERTY_DEFINITIONS.remove(propertyDefinition.getPropertyKey())
        );
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GuildProperties[\n");

        Map<String, Property> properties = getProperties();
        if (properties != null) {
            properties.forEach((s, o) -> builder.append('\t').append(s).append(" = ").append(o).append('\n'));
        } else {
            builder.append("null");
        }
        builder.append(']');

        return builder.toString();
    }
}
