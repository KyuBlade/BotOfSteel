package com.omega.database.repository;

import com.omega.database.entity.property.BotProperties;

public interface BotPropertiesRepository extends Repository<BotProperties> {

    BotProperties create();

    BotProperties get();
}
