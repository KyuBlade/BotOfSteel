package com.omega.property;

import com.omega.database.entity.property.IntProperty;
import com.omega.database.entity.property.NullProperty;
import com.omega.guild.property.PropertyDefinition;
import com.omega.guild.property.PropertySupplier;

public class CoreBotPropertySupplier implements PropertySupplier {

    public static final PropertyDefinition BOT_TOKEN = new PropertyDefinition(
        "bot_token", new NullProperty(), null);
    public static final PropertyDefinition SHARD_COUNT = new PropertyDefinition(
        "shard_count", new IntProperty(1), null);

    @Override
    public PropertyDefinition[] supply() {
        return new PropertyDefinition[]{
            BOT_TOKEN, SHARD_COUNT
        };
    }
}
