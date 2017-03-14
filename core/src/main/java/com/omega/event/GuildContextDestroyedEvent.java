package com.omega.event;

import com.omega.guild.GuildContext;
import sx.blah.discord.api.events.Event;

public class GuildContextDestroyedEvent extends Event {

    private final GuildContext guildContext;

    public GuildContextDestroyedEvent(GuildContext guildContext) {
        this.guildContext = guildContext;
    }

    public GuildContext getGuildContext() {
        return guildContext;
    }
}
