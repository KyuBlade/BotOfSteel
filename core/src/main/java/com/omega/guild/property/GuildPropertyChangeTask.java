package com.omega.guild.property;

import com.omega.database.entity.property.Property;
import com.omega.guild.GuildContext;

public interface GuildPropertyChangeTask<T extends Property> extends PropertyChangeTask<GuildContext, T> {

}
