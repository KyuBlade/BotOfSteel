package com.omega.database.entity.property;

public class NullProperty implements Property<Void> {

    public NullProperty() {
    }

    @Override
    public Void getValue() {
        return null;
    }
}
