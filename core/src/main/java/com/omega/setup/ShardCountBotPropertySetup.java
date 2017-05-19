package com.omega.setup;

import com.omega.database.entity.property.BotProperties;
import com.omega.database.entity.property.IntProperty;
import com.omega.property.CoreBotPropertySupplier;
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
