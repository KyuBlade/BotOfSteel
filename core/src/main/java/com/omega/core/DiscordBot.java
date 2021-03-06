package com.omega.core;

import com.omega.core.database.DatastoreManagerSingleton;
import org.slf4j.LoggerFactory;


public class DiscordBot {

    public static void main(String[] args) {
        // Force datastore initialization
        DatastoreManagerSingleton.getInstance();

        BotManager botManager = BotManager.getInstance();
        try {
            botManager.init();
            botManager.connect();
        } catch (Exception e) {
            LoggerFactory.getLogger(DiscordBot.class).error("Unhandled exception occurred", e);
        }
    }
}
