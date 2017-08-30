package com.omega.core.guild.property;

import com.omega.core.database.entity.property.Property;
import com.omega.core.guild.GuildContext;

public interface GuildPropertyChangeTask<T extends Property> extends PropertyChangeTask<GuildContext, T> {

}
