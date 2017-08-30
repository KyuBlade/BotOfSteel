package com.omega.core.guild.property;

import com.omega.core.module.Supplier;

public interface PropertySupplier extends Supplier<PropertyDefinition> {

    PropertyDefinition[] supply();
}
