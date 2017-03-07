package com.omega.database.entity.property;

public class BooleanProperty implements Property<Boolean> {

    private boolean value;

    public BooleanProperty() {
    }

    public BooleanProperty(Boolean value) {
        this.value = value;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}
