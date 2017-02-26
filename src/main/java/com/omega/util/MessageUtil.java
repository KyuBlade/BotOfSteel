package com.omega.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

public class MessageUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageUtil.class);

    /**
     * Reply to a message.
     *
     * @param messageToReply the message you want to reply to
     * @param content        the message content
     */
    public static void reply(IMessage messageToReply, String content) {
        RequestBuffer.request(() -> {
            try {
                messageToReply.reply(content);
            } catch (MissingPermissionsException e) {
                LOGGER.info("Permissions needed to reply", e);
            } catch (DiscordException e) {
                LOGGER.warn("Unable to send message", e);
            }
        });
    }

    /**
     * Reply to a message using an embed object.
     *
     * @param messageToReply the message you want to reply to
     * @param content        the message content
     * @param embedObject    the embed object to send
     */
    public static void reply(IMessage messageToReply, String content, EmbedObject embedObject) {
        RequestBuffer.request(() -> {
            try {
                messageToReply.reply(content, embedObject);
            } catch (MissingPermissionsException e) {
                LOGGER.info("Permissions needed to reply", e);
            } catch (DiscordException e) {
                LOGGER.warn("Unable to send message", e);
            }
        });
    }

    /**
     * Send a private message.
     *
     * @param by      user to message
     * @param content message content
     */
    public static void sendPrivateMessage(IUser by, String content) {
        RequestBuffer.request(() -> {
            try {
                by.getOrCreatePMChannel().sendMessage(content);
            } catch (MissingPermissionsException e) {
                LOGGER.info("Permissions needed to send private message", e);
            } catch (DiscordException e) {
                LOGGER.warn("Unable to send message", e);
            }
        });
    }

    /**
     * Send a message in a channel.
     *
     * @param channel channel to send to
     * @param content message content
     */
    public static void sendMessage(IChannel channel, String content) {
        RequestBuffer.request(() -> {
            try {
                channel.sendMessage(content);
            } catch (MissingPermissionsException e) {
                LOGGER.info("Permissions needed to send message on channel " + channel.getName(), e);
            } catch (DiscordException e) {
                LOGGER.warn("Unable to send message", e);
            }
        });
    }

    /**
     * Delete a message.
     *
     * @param message message to delete
     */
    public static void deleteMessage(IMessage message) {
        RequestBuffer.request(() -> {
            try {
                message.delete();
            } catch (MissingPermissionsException e) {
                LOGGER.info("Missing permissions", e);
            } catch (DiscordException e) {
                LOGGER.info("Error while deleting message", e);
            }
        });
    }
}
