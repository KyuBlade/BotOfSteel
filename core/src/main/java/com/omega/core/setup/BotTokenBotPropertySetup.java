package com.omega.core.setup;

import com.omega.core.database.entity.property.BotProperties;
import com.omega.core.database.entity.property.StringProperty;
import com.omega.core.property.CoreBotPropertySupplier;
import org.beryx.textio.TextIO;

public class BotTokenBotPropertySetup implements BotPropertySetup {

    @Override
    public void setup(TextIO textIO, BotProperties botProperties) {
        String botToken = textIO
            .newStringInputReader()
            .withInputMasking(true)
            .read("Bot token : ");

        botProperties.setProperty(CoreBotPropertySupplier.BOT_TOKEN, new StringProperty(botToken));
    }
}
