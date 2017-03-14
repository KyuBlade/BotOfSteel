package com.omega.guild.property;

import com.omega.module.Supplier;

public interface PropertySupplier extends Supplier<PropertyDefinition> {

    PropertyDefinition[] supply();
}
