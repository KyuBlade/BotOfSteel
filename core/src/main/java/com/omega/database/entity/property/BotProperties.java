package com.omega.database.entity.property;

import com.omega.BotManager;
import com.omega.database.DatastoreManagerSingleton;
import com.omega.database.repository.BotPropertiesRepository;
import com.omega.guild.property.PropertyChangeTask;
import com.omega.guild.property.PropertyDefinition;
import com.omega.guild.property.PropertySupplier;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class BotProperties extends Properties {

    public enum Fields {
        id, properties
    }

    private static final Map<String, PropertyDefinition> PROPERTY_DEFINITIONS = new HashMap<>();

    public abstract Object getId();

    @Override
    public Map<String, PropertyDefinition> getPropertyDefinitions() {
        return PROPERTY_DEFINITIONS;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void executePropertyChangeTask(PropertyChangeTask task, Property property, boolean init) {
        task.execute(BotManager.getInstance(), property, init);
    }

    @Override
    public void save() {
        BotPropertiesRepository repository = DatastoreManagerSingleton.getInstance()
            .getRepository(BotPropertiesRepository.class);
        repository.save(this);
    }

    /**
     * Add available property definitions.
     *
     * @param supplier property definition supplier with properties to add
     */
    public static void supply(PropertySupplier supplier) {
        PropertyDefinition[] propertyDefinitions = supplier.supply();
        Arrays.stream(propertyDefinitions).forEach(propertyDefinition ->
            PROPERTY_DEFINITIONS.put(propertyDefinition.getPropertyKey(), propertyDefinition)
        );
    }

    /**
     * Remove property previously added by a supplier.
     *
     * @param supplier property definition supplier with properties to remove
     */
    public static void unsupply(PropertySupplier supplier) {
        PropertyDefinition[] propertyDefinitions = supplier.supply();
        Arrays.stream(propertyDefinitions).forEach(propertyDefinition ->
            PROPERTY_DEFINITIONS.remove(propertyDefinition.getPropertyKey())
        );
    }
}
