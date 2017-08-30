package com.omega.core.guild.property;

import com.omega.core.database.entity.property.Property;

public class PropertyDefinition {

    private final String propertyKey;
    private final Property defaultProperty;
    private final PropertyChangeTask task;

    public PropertyDefinition(String propertyKey, Property defaultProperty, PropertyChangeTask task) {
        this.propertyKey = propertyKey;
        this.defaultProperty = defaultProperty;
        this.task = task;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    public Property getDefaultProperty() {
        return defaultProperty;
    }

    public PropertyChangeTask getTask() {
        return task;
    }

    @Override
    public int hashCode() {
        return propertyKey.hashCode();
    }
}
