package com.omega.core;

import com.omega.core.command.CommandManager;
import com.omega.core.command.CoreCommandSupplier;
import com.omega.core.database.DatastoreManagerSingleton;
import com.omega.core.database.entity.permission.CorePermissionSupplier;
import com.omega.core.database.entity.property.BotProperties;
import com.omega.core.database.entity.property.GuildProperties;
import com.omega.core.database.repository.BotPropertiesRepository;
import com.omega.core.guild.property.CoreGuildPropertySupplier;
import com.omega.core.listener.MessageListener;
import com.omega.core.listener.StateListener;
import com.omega.core.property.CoreBotPropertySupplier;
import com.omega.core.setup.BotSetup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.modules.Configuration;
import sx.blah.discord.util.DiscordException;

public class BotManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(BotManager.class);

    private boolean init;
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
        if (init) {
            LOGGER.info("BotManager already initialized");
            return;
        }

        Configuration.LOAD_EXTERNAL_MODULES = false;

        BotProperties.supply(new CoreBotPropertySupplier());

        BotPropertiesRepository repository = DatastoreManagerSingleton.getInstance()
            .getRepository(BotPropertiesRepository.class);

        this.botProperties = repository.get();
        if (this.botProperties == null) {
            BotSetup botSetup = new BotSetup();
            botSetup.setup();

            this.botProperties = botSetup.getBotProperties();
        }

        CommandManager.getInstance().supply(new CoreCommandSupplier());
        GuildProperties.supply(new CoreGuildPropertySupplier());
        PermissionManager.getInstance().supply(new CorePermissionSupplier());

        clientBuilder = new ClientBuilder();
        clientBuilder.withToken(botProperties.getProperty(CoreBotPropertySupplier.BOT_TOKEN, String.class))
            .withShards(botProperties.getProperty(CoreBotPropertySupplier.SHARD_COUNT, Integer.class));
        this.client = clientBuilder.build();

        EventDispatcher dispatcher = client.getDispatcher();
        dispatcher.registerListener(PermissionManager.getInstance());
        dispatcher.registerListener(new StateListener());
        dispatcher.registerListener(new MessageListener(client));
        dispatcher.registerListener(CommandManager.getInstance());
        dispatcher.registerListener(this);

        init = true;
    }

    public void connect() {
        try {
            client.login();
        } catch (DiscordException e) {
            LOGGER.error("Unable to connect the bot", e);
        }
    }

    @EventSubscriber
    public void onReady(ReadyEvent event) {
        try {
            this.applicationOwner = getClient().getApplicationOwner();
            LOGGER.debug("Application owner set : {}", applicationOwner.getName());
        } catch (DiscordException e) {
            LOGGER.warn("Unable to get application owner, retrying");
            onReady(event);
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
