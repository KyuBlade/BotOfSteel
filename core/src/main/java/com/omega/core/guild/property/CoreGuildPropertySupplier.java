package com.omega.core.guild.property;

import com.omega.core.database.entity.property.NullProperty;
import com.omega.core.database.entity.property.StringProperty;

public class CoreGuildPropertySupplier implements PropertySupplier {

    public static final PropertyDefinition COMMAND_PREFIX = new PropertyDefinition(
        "command_prefix", new StringProperty("!"), new CommandPrefixGuildPropertyChangeTask()
    );
    public static final PropertyDefinition AUTOROLL = new PropertyDefinition(
        "autoroll", new NullProperty(), null
    );

    @Override
    public PropertyDefinition[] supply() {
        return new PropertyDefinition[]{COMMAND_PREFIX, AUTOROLL};
    }
}
