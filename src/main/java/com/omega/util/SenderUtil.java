package com.omega.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.*;

import java.util.Timer;
import java.util.TimerTask;

public class SenderUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SenderUtil.class);

    public static void reply(IMessage messageToReply, String messageToSend) {
        RequestBuffer.request(() -> {
            try {
                messageToReply.reply(messageToSend);
            } catch (MissingPermissionsException e) {
                LOGGER.info("Permissions needed to reply", e);
            } catch (DiscordException e) {
                LOGGER.warn("Unable to send message", e);
            }
        });
    }

    public static void reply(IMessage messageToReply, String messageToSend, EmbedObject embedObject) {
        RequestBuffer.request(() -> {
            try {
                messageToReply.reply(messageToSend, embedObject);
            } catch (MissingPermissionsException e) {
                LOGGER.info("Permissions needed to reply", e);
            } catch (DiscordException e) {
                LOGGER.warn("Unable to send message", e);
            }
        });
    }

    public static void sendPrivateMessage(IUser by, String message) {
        RequestBuffer.request(() -> {
            try {
                by.getOrCreatePMChannel().sendMessage(message);
            } catch (MissingPermissionsException e) {
                LOGGER.info("Permissions needed to send private message", e);
            } catch (DiscordException e) {
                LOGGER.warn("Unable to send message", e);
            }
        });
    }

    public static void sendMessage(IChannel channel, String message) {
        RequestBuffer.request(() -> {
            try {
                channel.sendMessage(message);
            } catch (MissingPermissionsException e) {
                LOGGER.info("Permissions needed to send message on channel " + channel.getName(), e);
            } catch (DiscordException e) {
                LOGGER.warn("Unable to send message", e);
            }
        });
    }
}
