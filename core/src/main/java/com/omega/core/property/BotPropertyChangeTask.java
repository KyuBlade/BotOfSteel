package com.omega.core.property;

import com.omega.core.BotManager;
import com.omega.core.database.entity.property.Property;
import com.omega.core.guild.property.PropertyChangeTask;

public interface BotPropertyChangeTask<T extends Property> extends PropertyChangeTask<BotManager, T> {
}
