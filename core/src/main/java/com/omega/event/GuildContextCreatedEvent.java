package com.omega.event;

import com.omega.guild.GuildContext;
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
