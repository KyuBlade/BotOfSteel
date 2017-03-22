package com.omega.guild;


import com.omega.database.DatastoreManagerSingleton;
import com.omega.database.repository.GuildPropertiesRepository;
import com.omega.database.entity.property.GuildProperties;
import com.omega.event.GuildContextCreatedEvent;
import com.omega.event.GuildContextDestroyedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IGuild;

import java.util.HashMap;
import java.util.Map;

public class GuildContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuildContext.class);

    private final IGuild guild;
    private GuildProperties properties;

    private Map<String, GuildModuleComponent> moduleComponents;

    public GuildContext(IGuild guild) {
        this.guild = guild;

        moduleComponents = new HashMap<>();

        guild.getClient().getDispatcher().dispatch(new GuildContextCreatedEvent(this));

        GuildPropertiesRepository propertiesRepository = DatastoreManagerSingleton.getInstance().getRepository(GuildPropertiesRepository.class);
        this.properties = propertiesRepository.findByGuild(guild);
        if (properties != null) {
            properties.initLoad(this);
            LOGGER.debug("Saved properties found, {}", properties.toString());
        } else {
            LOGGER.debug("Loading default properties");
            this.properties = propertiesRepository.create(guild);
            properties.initDefault(this);
        }
    }

    public void destroy() {
        guild.getClient().getDispatcher().dispatch(new GuildContextDestroyedEvent(this));
    }

    public GuildModuleComponent getModuleComponent(String componentName) {
        return moduleComponents.get(componentName);
    }

    public void putModuleComponent(String componentName, GuildModuleComponent moduleComponent) {
        this.moduleComponents.put(componentName, moduleComponent);
    }

    public GuildModuleComponent removeModuleComponent(String componentName) {
        return this.moduleComponents.remove(componentName);
    }

    public GuildProperties getProperties() {
        return properties;
    }

    public IGuild getGuild() {
        return guild;
    }
}
