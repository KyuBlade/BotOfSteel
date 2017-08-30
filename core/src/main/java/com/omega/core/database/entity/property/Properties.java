package com.omega.core.database.entity.property;

import com.omega.core.database.DatastoreManagerSingleton;
import com.omega.core.database.repository.PropertyRepository;
import com.omega.core.exception.PropertyNotFoundException;
import com.omega.core.guild.property.PropertyChangeTask;
import com.omega.core.guild.property.PropertyDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class Properties {

    private static final Logger LOGGER = LoggerFactory.getLogger(Properties.class);

    private final Class<? extends PropertyRepository> repositoryType;

    protected Properties(Class<? extends PropertyRepository> repositoryType) {
        this.repositoryType = repositoryType;
    }

    public abstract Map<String, Property> getProperties();

    public abstract Map<String, PropertyDefinition> getPropertyDefinitions();

    protected abstract void executePropertyChangeTask(PropertyChangeTask task, Property property, boolean init);

    @SuppressWarnings("unchecked")
    public void save() {
        PropertyRepository repository = DatastoreManagerSingleton.getInstance()
            .getRepository(repositoryType);
        repository.save(this);
    }

    public void initLoad() {
        getProperties().forEach((propertyName, property) -> {
                PropertyDefinition propertyDefinition = getPropertyDefinitions().get(propertyName);
                if (propertyDefinition != null) {
                    PropertyChangeTask task = propertyDefinition.getTask();
                    if (task != null) {
                        try {
                            executePropertyChangeTask(task, property, false);
                        } catch (Exception e) {
                            LOGGER.warn("Exception occurred while executing property change task for property {}", propertyName, e);
                        }
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
        Property foundProperty = getProperties().get(property.getPropertyKey());
        if (foundProperty == null) {
            foundProperty = property.getDefaultProperty();
        }

        return (T) foundProperty.getValue();
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
        if (getPropertyDefinitions().containsKey(property)) {
            PropertyDefinition propertyEnum = getPropertyDefinitions().get(property);
            setProperty(propertyEnum, value, init);
        } else {
            throw new PropertyNotFoundException();
        }
    }
}
