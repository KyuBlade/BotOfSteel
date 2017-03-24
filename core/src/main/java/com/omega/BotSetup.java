package com.omega;

import com.omega.database.DatastoreManagerSingleton;
import com.omega.database.entity.property.BotProperties;
import com.omega.database.entity.property.IntProperty;
import com.omega.database.entity.property.StringProperty;
import com.omega.database.repository.BotPropertiesRepository;
import com.omega.property.CoreBotPropertySupplier;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

public class BotSetup {

    private BotProperties botProperties;

    public BotSetup() {
    }

    public void setup() {
        BotPropertiesRepository repository = DatastoreManagerSingleton.getInstance()
            .getRepository(BotPropertiesRepository.class);
        this.botProperties = repository.get();
        if (botProperties != null) {
            botProperties.initLoad();
        } else {
            this.botProperties = repository.create();
            botProperties.initDefault();

            TextIO textIO = TextIoFactory.getTextIO();
            textIO.getTextTerminal().println("First launch setup : ");
            String botToken = textIO
                .newStringInputReader()
                .withInputMasking(true)
                .read("Bot token : ");
            botProperties.setProperty(CoreBotPropertySupplier.BOT_TOKEN, new StringProperty(botToken));

            String botClientId = textIO
                .newStringInputReader()
                .read("Bot client id : ");
            botProperties.setProperty(CoreBotPropertySupplier.BOT_CLIENT_ID, new StringProperty(botClientId));

            int shardCount = textIO
                .newIntInputReader()
                .withMinVal(1)
                .withDefaultValue((Integer) CoreBotPropertySupplier.SHARD_COUNT.getDefaultProperty().getValue())
                .read("Shard count : ");
            botProperties.setProperty(CoreBotPropertySupplier.SHARD_COUNT, new IntProperty(shardCount));

            textIO.getTextTerminal().println("Bot setup finished");
            textIO.getTextTerminal().dispose();
        }
    }

    public BotProperties getBotProperties() {
        return botProperties;
    }
}
