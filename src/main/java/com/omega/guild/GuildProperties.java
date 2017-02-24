package com.omega.guild;

import com.omega.BotManager;
import com.omega.util.StringUtils;
import com.omega.database.DatastoreManagerSingleton;
import com.omega.database.GuildPropertiesRepository;
import com.omega.event.GuildPropertyChangedEvent;
import com.omega.exception.PropertyNotFoundException;
import de.caluga.morphium.annotations.*;
import de.caluga.morphium.annotations.lifecycle.Lifecycle;
import de.caluga.morphium.annotations.lifecycle.PostLoad;
import de.caluga.morphium.driver.bson.MorphiumId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IGuild;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Entity
@Index
@PartialUpdate
@Lifecycle
public class GuildProperties {

    public enum Fields {
        id, guildId, properties
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(GuildProperties.class);

    @Id
    private MorphiumId id;

    @Index
    private String guildId;

    @Transient
    private IGuild guild;

    private Map<String, Object> properties;

    private static final Map<String, Property> propertyList = new HashMap<>();

    static {
        Arrays.stream(Property.values()).forEach(property -> propertyList.put(property.getProperty(), property));
    }

    public GuildProperties() {
        this.properties = new HashMap<>();
    }

    public GuildProperties(IGuild guild) {
        this();

        this.guild = guild;
        this.guildId = guild.getID();
    }

    @PostLoad
    public void postLoad() {
        this.guild = BotManager.getInstance().getClient().getGuildByID(guildId);
        LOGGER.debug("Retrieve guild {}", guild.getName());
        properties.forEach((s, o) -> guild.getClient().getDispatcher().dispatch(
            new GuildPropertyChangedEvent(guild, propertyList.get(s), o, true))
        );
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(Property property, Class<T> type) {
        return (T) properties.get(property.getProperty());
    }

    public Object getProperty(String property) throws PropertyNotFoundException {
        if (validateProperty(property)) {
            return properties.get(property);
        } else {
            throw new PropertyNotFoundException();
        }
    }

    public void setProperty(Property property, Object value) {
        if (value == null || property.getType().isInstance(value)) {
            properties.put(property.getProperty(), value);
            save();

            guild.getClient().getDispatcher().dispatch(new GuildPropertyChangedEvent(guild, property, value));
        } else {
            LOGGER.warn(
                "Wrong value provided for property {}, was of type {}, must be of type {}",
                property,
                (value == null) ? "undefined" : value.getClass().getSimpleName(),
                property.getType().getSimpleName()
            );

            throw new IllegalArgumentException();
        }
    }

    public void setProperty(String property, Object value) throws PropertyNotFoundException, IllegalArgumentException {
        if (validateProperty(property)) {
            Property propertyEnum = propertyList.get(property);
            setProperty(propertyEnum, value);
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
        return propertyList.containsKey(property);
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GuildProperties[\n");
        if (properties != null) {
            properties.forEach((s, o) -> builder.append('\t').append(s).append(" = ").append(o).append('\n'));
        } else {
            builder.append("null");
        }
        builder.append(']');

        return builder.toString();
    }
}
