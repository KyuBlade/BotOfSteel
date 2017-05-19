package com.omega.database.entity.property;

public class StringProperty implements Property<String> {

    private String value;

    public StringProperty() {
    }

    public StringProperty(char[] value) {
        this.value = new String(value);
    }

    public StringProperty(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
