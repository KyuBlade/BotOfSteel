package com.omega.guild.property;

import com.omega.database.entity.property.Property;

public interface PropertyChangeTask<T, U extends Property> {

    void execute(T context, U property, boolean init);
}
