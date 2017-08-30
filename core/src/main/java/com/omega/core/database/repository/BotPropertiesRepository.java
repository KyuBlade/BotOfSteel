package com.omega.core.database.repository;

import com.omega.core.database.entity.property.BotProperties;

public interface BotPropertiesRepository extends PropertyRepository<BotProperties> {

    BotProperties get();
}
