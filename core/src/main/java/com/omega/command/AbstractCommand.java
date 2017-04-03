package com.omega.command;

import com.omega.util.DiscordUtils;
import com.omega.util.MessageUtils;
import com.omega.util.MessageWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.util.EnumSet;

public abstract class AbstractCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCommand.class);

    protected final String name;

    protected IUser botUser;
    protected IGuild guild;
    protected IUser by;
    protected IChannel channel;
    protected IMessage message;

    protected MessageWrapper stateMessage;

    public AbstractCommand(IUser by, IMessage message) {
        this.botUser = by.getClient().getOurUser();
        this.guild = message.getGuild();
        this.channel = message.getChannel();
        this.by = by;
        this.message = message;

        Command commandAnnotation = getClass().getAnnotation(Command.class);
        this.name = commandAnnotation.name();
    }

    public MessageWrapper sendErrorMessage(String description) {
        return sendStateMessage(
            new EmbedBuilder()
                .withDescription(description)
                .build(),
            Color.RED
        );
    }

    public MessageWrapper sendErrorMessage(String title, String description) {
        return sendErrorMessage(
            new EmbedBuilder()
                .withTitle(title)
                .withDescription(description)
                .build()
        );
    }

    public MessageWrapper sendErrorMessage(EmbedObject embedObject) {
        return sendStateMessage(embedObject, Color.RED);
    }

    public MessageWrapper sendStateMessage(EmbedObject embedObject) {
        return sendStateMessage(embedObject, Color.GREEN);
    }

    public MessageWrapper sendStateMessage(String description) {
        return sendStateMessage(null, description);
    }

    public MessageWrapper sendStateMessage(String description, Color color) {
        return sendStateMessage(null, description, color);
    }

    public MessageWrapper sendStateMessage(String title, String description) {
        return sendStateMessage(title, description, Color.GREEN);
    }

    public MessageWrapper sendStateMessage(String title, String description, Color color) {
        return sendStateMessage(
            new EmbedBuilder()
                .withTitle(title)
                .withDescription(description)
                .build(),
            color
        );
    }

    public MessageWrapper sendStateMessage(EmbedObject embedObject, Color color) {
        return sendStateMessage(embedObject, color, false);
    }

    public MessageWrapper sendStateMessage(EmbedObject embedObject, Color color, boolean isPrivate) {
        embedObject.footer = new EmbedObject.FooterObject(
            String.format("Requested by @%s#%s", by.getName(), by.getDiscriminator()),
            by.getAvatarURL(),
            null
        );

        embedObject.color = DiscordUtils.getColorInteger(color);

        if (embedObject.author == null) {
            IUser botUser = by.getClient().getOurUser();
            embedObject.author = new EmbedObject.AuthorObject(
                botUser.getName(), null, botUser.getAvatarURL(), null
            );
        }

        if (stateMessage == null) {
            if (isPrivate) {
                this.stateMessage = MessageUtils.sendPrivateMessage(by, embedObject);
            } else {
                this.stateMessage = MessageUtils.sendMessage(channel, embedObject);
            }
        } else {
            stateMessage.editMessage(embedObject);
        }

        return stateMessage;
    }

    public MessageWrapper sendExceptionMessage(String title, String description, Throwable t, IEmbed.IEmbedField... fields) {
        LOGGER.error(description);

        return MessageUtils.sendMessage(channel, title, description, t, fields);
    }

    public MessageWrapper sendExceptionMessage(String title, String description, Throwable t) {
        LOGGER.error(description);

        return MessageUtils.sendMessage(channel, title, description, t);
    }

    public MessageWrapper sendExceptionMessage(String description, Throwable t) {
        return sendExceptionMessage(null, description, t);
    }

    public void sendMissingPermissionsMessage(EnumSet<Permissions> missingPermissions) {
        sendMissingPermissionsMessage(null, missingPermissions);
    }

    public void sendMissingPermissionsMessage(String description, EnumSet<Permissions> missingPermissions) {
        MessageUtils.sendMissingPermissionsMessage(channel, description, missingPermissions);
    }

    public MessageWrapper sendPrivateStateMessage(EmbedObject embedObject) {
        return sendStateMessage(embedObject, Color.GREEN, true);
    }

    public MessageWrapper sendPrivateStateMessage(String description) {
        return sendPrivateStateMessage(null, description);
    }

    public MessageWrapper sendPrivateStateMessage(String title, String description) {
        return sendPrivateStateMessage(
            new EmbedBuilder()
                .withTitle(title)
                .withDescription(description)
                .build()
        );
    }

    public MessageWrapper sendPrivateStateMessage(EmbedObject embedObject, Color color) {
        return sendStateMessage(embedObject, color, false);
    }
}
