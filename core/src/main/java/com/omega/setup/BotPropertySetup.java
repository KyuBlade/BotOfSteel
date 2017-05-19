package com.omega.setup;

import com.omega.database.entity.property.BotProperties;
import org.beryx.textio.TextIO;

public interface BotPropertySetup {

    void setup(TextIO textIO, BotProperties botProperties);
}
