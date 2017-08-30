package com.omega.core.setup;

import com.omega.core.database.entity.property.BotProperties;
import com.omega.core.database.entity.property.IntProperty;
import com.omega.core.property.CoreBotPropertySupplier;
import org.beryx.textio.TextIO;

public class ShardCountBotPropertySetup implements BotPropertySetup {

    @Override
    public void setup(TextIO textIO, BotProperties botProperties) {
        int shardCount = textIO
            .newIntInputReader()
            .withMinVal(1)
            .withDefaultValue((Integer) CoreBotPropertySupplier.SHARD_COUNT.getDefaultProperty().getValue())
            .read("Shard count : ");

        botProperties.setProperty(CoreBotPropertySupplier.SHARD_COUNT, new IntProperty(shardCount));
    }
}
