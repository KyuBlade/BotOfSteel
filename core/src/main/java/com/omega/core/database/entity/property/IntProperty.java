package com.omega.core.database.entity.property;

public class IntProperty implements Property<Integer> {

    private int value;

    public IntProperty() {
    }

    public IntProperty(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
