package com.omega.database.entity.property;

import com.omega.database.DatastoreManagerSingleton;
import com.omega.database.GuildPropertiesRepository;
import com.omega.exception.PropertyNotFoundException;
import com.omega.guild.GuildContext;
import com.omega.guild.property.PropertyDefinition;
import com.omega.guild.property.PropertySupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IGuild;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class GuildProperties {

    public enum Fields {
        id, guild, properties
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(GuildProperties.class);

    private static final Map<String, PropertyDefinition> PROPERTY_DEFINITIONS = new HashMap<>();

    public GuildProperties() {
    }

    public abstract Object getId();

    public abstract IGuild getGuild();

    protected abstract GuildContext getGuildContext();

    protected abstract void setGuildContext(GuildContext guildContext);

    public abstract Map<String, Property> getProperties();

    @SuppressWarnings("unchecked")
    public void initLoad(GuildContext guildContext) {
        setGuildContext(guildContext);

        getProperties().forEach((propertyName, property) -> {
                PropertyDefinition propertyDefinition = PROPERTY_DEFINITIONS.get(propertyName);
                if (propertyDefinition != null) {
                    propertyDefinition.getTask().execute(guildContext, property, false);
                } else {
                    LOGGER.debug("Property {} not found", propertyName);
                }
            }
        );
    }

    public void initDefault(GuildContext guildContext) {
        setGuildContext(guildContext);

        GuildProperties.PROPERTY_DEFINITIONS.values().forEach(propDef ->
            setProperty(propDef, propDef.getDefaultProperty(), true)
        );
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(PropertyDefinition property, Class<T> type) {
        return (T) getProperties().get(property.getPropertyKey()).getValue();
    }

    public Object getProperty(String property) throws PropertyNotFoundException {
        Map<String, Property> properties = getProperties();
        if (property == null) {
            return null;
        } else if (properties.containsKey(property)) {
            return getProperties().get(property);
        } else {
            throw new PropertyNotFoundException();
        }
    }

    public void setProperty(PropertyDefinition propertyDefinition, Property value) {
        setProperty(propertyDefinition, value);
    }

    @SuppressWarnings("unchecked")
    public void setProperty(PropertyDefinition propertyDefinition, Property value, boolean init) {
        getProperties().put(propertyDefinition.getPropertyKey(), value);
        propertyDefinition.getTask().execute(getGuildContext(), value, init);
        save();
    }

    public void setProperty(String property, Property value) throws PropertyNotFoundException {
        setProperty(property, value, false);
    }

    public void setProperty(String property, Property value, boolean init) throws PropertyNotFoundException {
        if (getProperties().containsKey(property)) {
            PropertyDefinition propertyEnum = PROPERTY_DEFINITIONS.get(property);
            setProperty(propertyEnum, value, init);
        } else {
            throw new PropertyNotFoundException();
        }
    }

    public void save() {
        GuildPropertiesRepository propertiesRepository = DatastoreManagerSingleton.getInstance().getRepository(GuildPropertiesRepository.class);
        propertiesRepository.save(this);
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
