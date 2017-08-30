package com.omega.core.database.entity.property;

public class DoubleProperty implements Property<Double> {

    private double value;

    public DoubleProperty() {
    }

    public DoubleProperty(Double value) {
        this.value = value;
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
