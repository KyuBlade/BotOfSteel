package com.omega.core.event;

import com.omega.core.guild.GuildContext;
import sx.blah.discord.api.events.Event;

public class GuildContextCreatedEvent extends Event {

    private final GuildContext guildContext;

    public GuildContextCreatedEvent(GuildContext guildContext) {
        this.guildContext = guildContext;
    }

    public GuildContext getGuildContext() {
        return guildContext;
    }
}
