package com.omega.database.entity.property;

import com.omega.exception.PropertyNotFoundException;
import com.omega.guild.property.PropertyChangeTask;
import com.omega.guild.property.PropertyDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class Properties {

    private static final Logger LOGGER = LoggerFactory.getLogger(Properties.class);

    public abstract Map<String, Property> getProperties();

    public abstract Map<String, PropertyDefinition> getPropertyDefinitions();

    protected abstract void executePropertyChangeTask(PropertyChangeTask task, Property property, boolean init);

    public abstract void save();

    public void initLoad() {
        getProperties().forEach((propertyName, property) -> {
                PropertyDefinition propertyDefinition = getPropertyDefinitions().get(propertyName);
                if (propertyDefinition != null) {
                    PropertyChangeTask task = propertyDefinition.getTask();
                    if (task != null) {
                        executePropertyChangeTask(task, property, false);
                    }
                } else {
                    LOGGER.debug("Property {} not found", propertyName);
                }
            }
        );
    }

    public void initDefault() {
        getPropertyDefinitions().values().forEach(propDef ->
            setProperty(propDef, propDef.getDefaultProperty(), true)
        );
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(PropertyDefinition property, Class<T> type) {
        return (T) getProperties().get(property.getPropertyKey()).getValue();
    }

    public Object getProperty(String property) throws PropertyNotFoundException {
        Map<String, Property> properties = getProperties();
        if (property == null) {
            return null;
        } else if (properties.containsKey(property)) {
            return getProperties().get(property);
        } else {
            throw new PropertyNotFoundException();
        }
    }

    public void setProperty(PropertyDefinition propertyDefinition, Property value) {
        setProperty(propertyDefinition, value, false);
    }

    @SuppressWarnings("unchecked")
    public void setProperty(PropertyDefinition propertyDefinition, Property value, boolean init) {
        getProperties().put(propertyDefinition.getPropertyKey(), value);
        PropertyChangeTask propertyChangeTask = propertyDefinition.getTask();
        if (propertyChangeTask != null) {
            executePropertyChangeTask(propertyDefinition.getTask(), value, init);
        }
        save();
    }

    public void setProperty(String property, Property value) throws PropertyNotFoundException {
        setProperty(property, value, false);
    }

    public void setProperty(String property, Property value, boolean init) throws PropertyNotFoundException {
        if (getProperties().containsKey(property)) {
            PropertyDefinition propertyEnum = getPropertyDefinitions().get(property);
            setProperty(propertyEnum, value, init);
        } else {
            throw new PropertyNotFoundException();
        }
    }
}
