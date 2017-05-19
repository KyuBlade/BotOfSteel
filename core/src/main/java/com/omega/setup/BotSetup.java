package com.omega.setup;

import com.omega.database.DatastoreManagerSingleton;
import com.omega.database.entity.property.BotProperties;
import com.omega.database.repository.BotPropertiesRepository;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BotSetup {

    private static final List<BotPropertySetup> propertySetups = new ArrayList<>();

    private BotProperties botProperties;

    public BotSetup() {
    }

    public void setup() {
        BotPropertiesRepository repository = DatastoreManagerSingleton.getInstance()
            .getRepository(BotPropertiesRepository.class);

        this.botProperties = repository.create();


        TextIO textIO = TextIoFactory.getTextIO();
        textIO.getTextTerminal().println("First launch setup : ");

        propertySetups.forEach(botPropertySetup -> botPropertySetup.setup(textIO, this.botProperties));

        textIO.getTextTerminal().println("Bot setup finished");
        textIO.getTextTerminal().dispose();
    }

    public BotProperties getBotProperties() {
        return botProperties;
    }

    public static void supply(BotPropertySetupSupplier supplier) {
        Collections.addAll(propertySetups, supplier.supply());
    }

    public static void unsupply(BotPropertySetupSupplier supplier) {
        propertySetups.removeAll(Arrays.asList(supplier.supply()));
    }
}
