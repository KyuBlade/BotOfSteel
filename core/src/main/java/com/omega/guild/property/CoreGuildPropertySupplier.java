package com.omega.guild.property;

import com.omega.database.entity.property.StringProperty;

public class CoreGuildPropertySupplier implements PropertySupplier {

    public static final PropertyDefinition COMMAND_PREFIX = new PropertyDefinition(
        "command_prefix", new StringProperty("!"), new CommandPrefixGuildPropertyChangeTask()
    );

    @Override
    public PropertyDefinition[] supply() {
        return new PropertyDefinition[]{COMMAND_PREFIX};
    }
}
