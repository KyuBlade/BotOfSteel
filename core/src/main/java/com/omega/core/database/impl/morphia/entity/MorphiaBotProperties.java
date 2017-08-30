package com.omega.core.database.impl.morphia.entity;

import com.omega.core.database.entity.property.BotProperties;
import com.omega.core.database.entity.property.Property;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.HashMap;
import java.util.Map;

@Entity(value = "bot_properties", noClassnameStored = true)
public class MorphiaBotProperties extends BotProperties {

    @Id
    private ObjectId id;

    @Embedded
    private Map<String, Property> properties;

    public MorphiaBotProperties() {
        this.properties = new HashMap<>();
    }

    @Override
    public ObjectId getId() {
        return id;
    }

    public Map<String, Property> getProperties() {
        return properties;
    }
}
