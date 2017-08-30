package com.omega.core.event;

import com.omega.core.guild.GuildContext;
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
