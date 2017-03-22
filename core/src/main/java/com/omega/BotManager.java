package com.omega;

import com.omega.command.CommandManager;
import com.omega.command.CoreCommandSupplier;
import com.omega.config.BotConfig;
import com.omega.config.ConfigurationManager;
import com.omega.database.entity.permission.CorePermissionSupplier;
import com.omega.database.entity.property.GuildProperties;
import com.omega.guild.property.CorePropertySupplier;
import com.omega.listener.MessageListener;
import com.omega.listener.StateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactoryConfigurationException;
import java.io.IOException;

public class BotManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(BotManager.class);

    private ClientBuilder clientBuilder;
    private IDiscordClient client;
    private IUser applicationOwner;

    private BotManager() {
    }

    public static BotManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init() throws Exception {
        try {
            LOGGER.info("Load configuration file");
            ConfigurationManager.getInstance().load();
        } catch (ParserConfigurationException | XPathFactoryConfigurationException | SAXException | IOException | XPathExpressionException e) {
            throw new Exception("Unable to load bot configuration", e);
        }

        BotConfig config = BotConfig.getInstance();

        CommandManager.getInstance().supply(new CoreCommandSupplier());
        GuildProperties.supply(new CorePropertySupplier());
        PermissionManager.getInstance().supply(new CorePermissionSupplier());

        clientBuilder = new ClientBuilder();
        clientBuilder.withToken(config.getBotToken())
            .withShards(config.getShards());
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
