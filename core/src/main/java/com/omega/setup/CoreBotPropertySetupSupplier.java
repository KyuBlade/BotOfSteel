package com.omega.setup;

public class CoreBotPropertySetupSupplier implements BotPropertySetupSupplier {

    private static final BotTokenBotPropertySetup BOT_TOKEN_BOT_PROPERTY_SETUP = new BotTokenBotPropertySetup();
    private static final ShardCountBotPropertySetup SHARD_COUNT_BOT_PROPERTY_SETUP = new ShardCountBotPropertySetup();

    @Override
    public BotPropertySetup[] supply() {
        return new BotPropertySetup[]{BOT_TOKEN_BOT_PROPERTY_SETUP, SHARD_COUNT_BOT_PROPERTY_SETUP};
    }
}
