package com.omega.database.repository;

import com.omega.database.entity.property.BotProperties;

public interface BotPropertiesRepository extends PropertyRepository<BotProperties> {

    BotProperties get();
}
