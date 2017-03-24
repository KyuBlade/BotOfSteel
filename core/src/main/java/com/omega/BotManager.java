package com.omega;

import com.omega.command.CommandManager;
import com.omega.command.CoreCommandSupplier;
import com.omega.database.entity.permission.CorePermissionSupplier;
import com.omega.database.entity.property.BotProperties;
import com.omega.database.entity.property.GuildProperties;
import com.omega.guild.property.CoreGuildPropertySupplier;
import com.omega.listener.MessageListener;
import com.omega.listener.StateListener;
import com.omega.property.CoreBotPropertySupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;

public class BotManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(BotManager.class);

    private BotProperties botProperties;
    private ClientBuilder clientBuilder;
    private IDiscordClient client;
    private IUser applicationOwner;

    private BotManager() {
    }

    public static BotManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init() throws Exception {
        BotProperties.supply(new CoreBotPropertySupplier());
        BotSetup botSetup = new BotSetup();
        botSetup.setup();
        this.botProperties = botSetup.getBotProperties();

        CommandManager.getInstance().supply(new CoreCommandSupplier());
        GuildProperties.supply(new CoreGuildPropertySupplier());
        PermissionManager.getInstance().supply(new CorePermissionSupplier());

        clientBuilder = new ClientBuilder();
        clientBuilder.withToken(botProperties.getProperty(CoreBotPropertySupplier.BOT_TOKEN, String.class))
            .withShards(botProperties.getProperty(CoreBotPropertySupplier.SHARD_COUNT, Integer.class));
        this.client = clientBuilder.build();
    }

    public void connect() {
        try {
            client.login();
            EventDispatcher dispatcher = client.getDispatcher();
            dispatcher.registerListener(PermissionManager.getInstance());
            dispatcher.registerListener(new StateListener());
            dispatcher.registerListener(new MessageListener(client));
            dispatcher.registerListener(CommandManager.getInstance());
            dispatcher.registerListener(this);
        } catch (DiscordException e) {
            LOGGER.error("Unable to connect the bot", e);
        }
    }

    @EventSubscriber
    public void postConnect(ReadyEvent event) {
        try {
            this.applicationOwner = getClient().getApplicationOwner();
            LOGGER.debug("Application owner set : {}", applicationOwner.getName());
        } catch (DiscordException e) {
            LOGGER.warn("Unable to get application owner, retrying");
            postConnect(event);
        }
    }

    public BotProperties getBotProperties() {
        return botProperties;
    }

    public void registerEventListener(Object listener) {
        client.getDispatcher().registerListener(listener);
    }

    public IDiscordClient getClient() {
        return client;
    }

    public IUser getApplicationOwner() {
        return applicationOwner;
    }

    private static class SingletonHolder {
        private static BotManager INSTANCE = new BotManager();
    }
}
