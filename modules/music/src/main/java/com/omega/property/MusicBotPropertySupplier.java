package com.omega.property;

import com.omega.database.entity.property.NullProperty;
import com.omega.guild.property.PropertyDefinition;
import com.omega.guild.property.PropertySupplier;

public class MusicBotPropertySupplier implements PropertySupplier {

    public static final PropertyDefinition YT_API_KEY = new PropertyDefinition(
        "yt_api_key", new NullProperty(), null);

    @Override
    public PropertyDefinition[] supply() {
        return new PropertyDefinition[]{YT_API_KEY};
    }
}
