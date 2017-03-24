package com.omega.guild.property;

import com.omega.database.entity.property.StringProperty;
import com.omega.guild.GuildContext;

public class CommandPrefixGuildPropertyChangeTask implements GuildPropertyChangeTask<StringProperty> {

    @Override
    public void execute(GuildContext context, StringProperty property, boolean init) {
    }
}
