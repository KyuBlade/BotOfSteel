package com.omega.core.guild.property;

import com.omega.core.database.entity.property.StringProperty;
import com.omega.core.guild.GuildContext;

public class CommandPrefixGuildPropertyChangeTask implements GuildPropertyChangeTask<StringProperty> {

    @Override
    public void execute(GuildContext context, StringProperty property, boolean init) {
    }
}
