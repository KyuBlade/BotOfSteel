package com.omega.guild.property;

import com.omega.database.entity.property.*;

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
