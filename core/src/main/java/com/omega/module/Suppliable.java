package com.omega.module;

public interface Suppliable<T extends Supplier> {

    void supply(T supplier);

    void unsupply(T supplier);
}
