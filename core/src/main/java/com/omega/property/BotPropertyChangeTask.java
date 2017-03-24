package com.omega.property;

import com.omega.BotManager;
import com.omega.database.entity.property.Property;
import com.omega.guild.property.PropertyChangeTask;

public interface BotPropertyChangeTask<T extends Property> extends PropertyChangeTask<BotManager, T> {
}
