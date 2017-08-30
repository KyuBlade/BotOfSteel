package com.omega.core.setup;

import com.omega.core.database.entity.property.BotProperties;
import org.beryx.textio.TextIO;

public interface BotPropertySetup {

    void setup(TextIO textIO, BotProperties botProperties);
}
