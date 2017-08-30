package com.omega.core.module;

public interface Suppliable<T extends Supplier> {

    void supply(T supplier);

    void unsupply(T supplier);
}
