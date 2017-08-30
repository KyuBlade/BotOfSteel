package com.omega.core.guild.property;

import com.omega.core.database.entity.property.Property;

public interface PropertyChangeTask<T, U extends Property> {

    void execute(T context, U property, boolean init);
}
