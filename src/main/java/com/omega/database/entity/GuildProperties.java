package com.omega.database.entity;

import com.omega.database.DatastoreManagerSingleton;
import com.omega.database.GuildPropertiesRepository;
import com.omega.database.entity.property.Property;
import com.omega.exception.PropertyNotFoundException;
import com.omega.guild.GuildContext;
import com.omega.guild.property.PropertyDefinition;
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

    static {
        Arrays.stream(PropertyDefinition.values()).forEach(property -> PROPERTY_DEFINITIONS.put(property.getPropertyKey(), property));
    }

    public GuildProperties() {
    }

    public abstract Object getId();

    public abstract IGuild getGuild();

    protected abstract GuildContext getGuildContext();

    protected abstract void setGuildContext(GuildContext guildContext);

    public abstract Map<String, Property> getProperties();

    public void initLoad(GuildContext guildContext) {
        setGuildContext(guildContext);

        getProperties().forEach((s, o) -> {
            PropertyDefinition propertyDefinition = GuildProperties.getPropertyDefinitions().get(s);
            if (propertyDefinition != null) {
                //noinspection unchecked
                propertyDefinition.getTask().execute(getGuildContext(), o, true);
            } else {
                LOGGER.warn("Property definition {} not found", s);
            }
        });
    }

    public void initDefault(GuildContext guildContext) {
        setGuildContext(guildContext);

        Arrays.stream(PropertyDefinition.values()).forEach(property -> setProperty(property, property.getDefaultProperty(), true));
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(PropertyDefinition property, Class<T> type) {
        return (T) getProperties().get(property.getPropertyKey()).getValue();
    }

    public Object getProperty(String property) throws PropertyNotFoundException {
        if (validateProperty(property)) {
            return getProperties().get(property);
        } else {
            throw new PropertyNotFoundException();
        }
    }

    public void setProperty(PropertyDefinition propertyDefinition, Property value) {
        setProperty(propertyDefinition, value);
    }

    public void setProperty(PropertyDefinition propertyDefinition, Property value, boolean init) {
        getProperties().put(propertyDefinition.getPropertyKey(), value);
        //noinspection unchecked
        propertyDefinition.getTask().execute(getGuildContext(), value, init);
        save();
    }

    public void setProperty(String property, Property value) throws PropertyNotFoundException {
        setProperty(property, value, false);
    }

    public void setProperty(String property, Property value, boolean init) throws PropertyNotFoundException {
        if (validateProperty(property)) {
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
     * @return if the property key is a valid property
     */
    private boolean validateProperty(String property) {
        return PROPERTY_DEFINITIONS.containsKey(property);
    }

    public static Map<String, PropertyDefinition> getPropertyDefinitions() {
        return PROPERTY_DEFINITIONS;
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
