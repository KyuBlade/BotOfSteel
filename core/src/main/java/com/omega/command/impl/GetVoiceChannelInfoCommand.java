package com.omega.command.impl;

import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Permission;
import com.omega.command.Signature;
import com.omega.database.entity.permission.CorePermissionSupplier;
import com.omega.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.IVoiceState;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

@Command(name = "getChannelInfo", aliases = "gci")
public class GetVoiceChannelInfoCommand extends AbstractCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetVoiceChannelInfoCommand.class);

    public GetVoiceChannelInfoCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = CorePermissionSupplier.COMMAND_GETVOICECHANNELINFO)
    @Signature(help = "Get the connected voice channel info")
    public void getChannelInfo() {
        IVoiceState voiceState = by.getVoiceStateForGuild(message.getGuild());
        IVoiceChannel voiceChannel = voiceState.getChannel();
        if (voiceChannel != null) {
            MessageUtil.reply(message, printChannelInfo(voiceChannel));
        } else {
            MessageUtil.reply(message, "Not connected to a voice channel");
        }
    }

    private String printChannelInfo(IVoiceChannel voiceChannel) {
        StringBuilder builder = new StringBuilder();
        builder.append(MessageBuilder.Styles.CODE_WITH_LANG.getMarkdown())
            .append("Name : ").append(voiceChannel.getName()).append('\n')
            .append("Topic : ").append(voiceChannel.getTopic()).append('\n')
            .append("Creation date : ").append(voiceChannel.getCreationDate().toString()).append('\n')
            .append("User limits : ").append(voiceChannel.getUserLimit()).append('\n')
            .append("Connected users : ").append(voiceChannel.getConnectedUsers().size()).append('\n')
            .append("Bitrate : ").append(voiceChannel.getBitrate()).append('\n');

        String invitesCount = "N/A";
        try {
            invitesCount = String.valueOf(voiceChannel.getInvites().size());
        } catch (NullPointerException e) {
            invitesCount = "Need to fix NPE";
        } catch (DiscordException e) {
            LOGGER.warn("Unable to get invite count", e);
        } catch (RateLimitException e) {
            invitesCount = "Rate limit exceeded";
            LOGGER.warn("Unable to get invite count", e);
        } catch (MissingPermissionsException e) {
            invitesCount = "Permission needed";
            LOGGER.warn("Unable to get invite count", e);
        }
        builder.append("Current invites : ").append(invitesCount)
            .append(MessageBuilder.Styles.CODE_WITH_LANG.getReverseMarkdown());
        return builder.toString();
    }
}
