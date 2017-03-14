package com.omega.guild.property;

import com.omega.database.entity.property.StringProperty;

public class CorePropertySupplier implements PropertySupplier {

    public static final PropertyDefinition COMMAND_PREFIX = new PropertyDefinition(
        "command_prefix", new StringProperty("!"), new CommandPrefixPropertyChangeTask()
    );

    @Override
    public PropertyDefinition[] supply() {
        return new PropertyDefinition[]{COMMAND_PREFIX};
    }
}
