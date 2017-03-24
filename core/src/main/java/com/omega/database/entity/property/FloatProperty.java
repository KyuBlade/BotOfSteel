package com.omega.database.entity.property;

public class FloatProperty implements Property<Float> {

    private float value;

    public FloatProperty() {
    }

    public FloatProperty(Float value) {
        this.value = value;
    }

    @Override
    public Float getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Float.toString(value);
    }
}
