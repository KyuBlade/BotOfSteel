package com.omega.guild.property;

import com.omega.Supplier;

public interface PropertySupplier extends Supplier<PropertyDefinition> {

    PropertyDefinition[] supply();
}
