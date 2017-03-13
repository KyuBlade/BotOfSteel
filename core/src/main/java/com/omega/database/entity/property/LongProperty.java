package com.omega.database.entity.property;

public class LongProperty implements Property<Long> {

    private Long value;

    public LongProperty() {
    }

    public LongProperty(Long value) {
        this.value = value;
    }

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }
}
