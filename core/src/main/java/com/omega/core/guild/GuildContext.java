package com.omega.core.guild;


import com.omega.core.database.DatastoreManagerSingleton;
import com.omega.core.database.entity.property.GuildProperties;
import com.omega.core.database.repository.GuildPropertiesRepository;
import com.omega.core.event.GuildContextCreatedEvent;
import com.omega.core.event.GuildContextDestroyedEvent;
import com.omega.core.guild.property.CoreGuildPropertySupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

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

        EventDispatcher dispatcher = guild.getClient().getDispatcher();
        dispatcher.dispatch(new GuildContextCreatedEvent(this));
        dispatcher.registerListener(this);
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

    @EventSubscriber
    public void onUserJoin(UserJoinEvent event) {
        IGuild guild = event.getGuild();
        if (guild.equals(this.guild)) {
            GuildContext guildContext = GuildManager.getInstance().getContext(guild);
            IRole role = guildContext.getProperties().getProperty(CoreGuildPropertySupplier.AUTOROLL, IRole.class);
            if (role != null) {
                IUser user = event.getUser();
                user.addRole(role);
            }
        }
    }

    @EventSubscriber
    public void onReady(ReadyEvent event) {
        GuildPropertiesRepository propertiesRepository = DatastoreManagerSingleton.getInstance().getRepository(GuildPropertiesRepository.class);
        this.properties = propertiesRepository.findByGuild(guild);
        if (properties != null) {
            properties.initLoad(this);
        } else {
            this.properties = propertiesRepository.create(guild);
            properties.initDefault(this);
        }
    }
}
