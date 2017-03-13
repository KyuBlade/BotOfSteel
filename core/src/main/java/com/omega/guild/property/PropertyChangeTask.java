package com.omega.guild.property;

import com.omega.database.entity.property.Property;
import com.omega.guild.GuildContext;

public interface PropertyChangeTask<T extends Property> {

    void execute(GuildContext context, T property, boolean init);
}
