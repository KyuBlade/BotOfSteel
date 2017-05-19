package com.omega.guild;

import sx.blah.discord.handle.obj.IGuild;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GuildManager {

    private final Map<Long, GuildContext> guilds = new HashMap<>();

    private GuildManager() {
    }

    public static GuildManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public GuildContext getContext(IGuild guild) {
        if (guild == null) {
            return null;
        }

        if (guilds.containsKey(guild.getLongID())) {
            return guilds.get(guild.getLongID());
        } else {
            GuildContext context = new GuildContext(guild);
            guilds.put(guild.getLongID(), context);

            return context;
        }
    }

    public Collection<GuildContext> getGuildContexts() {
        return guilds.values();
    }

    private static class SingletonHolder {
        private static final GuildManager INSTANCE = new GuildManager();
    }
}
