package com.omega.command.impl;

import com.omega.MusicPermissionSupplier;
import com.omega.command.*;
import com.omega.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.MissingPermissionsException;

@Command(name = "join", aliases = "j")
public class JoinCommand extends AbstractCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(JoinCommand.class);

    public JoinCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = MusicPermissionSupplier.COMMAND_JOIN)
    @Signature(help = "The bot will join your current voice channel")
    public void joinCommand() {
        IVoiceState voiceState = by.getVoiceStateForGuild(message.getGuild());
        IVoiceChannel connectedVoiceChannel = voiceState.getChannel();
        if (connectedVoiceChannel != null) {
            join(connectedVoiceChannel);
        } else {
            MessageUtil.reply(message, "You are not connected to a voice channel");
        }
    }

    @Permission(permission = MusicPermissionSupplier.COMMAND_JOIN)
    @Signature(help = "Bot will join the specified voice channel")
    public void joinCommand(@Parameter(name = "voiceChannelName") String voiceChannelName) {
        IVoiceChannel voiceChannel = message.getGuild().getVoiceChannels().stream()
            .filter(channel -> channel.getName().equalsIgnoreCase(voiceChannelName))
            .findFirst()
            .orElse(null);

        if (voiceChannel != null) {
            join(voiceChannel);
        } else {
            MessageUtil.reply(message, "Voice channel " + voiceChannelName + " not found");
        }
    }

    private void join(IVoiceChannel voiceChannel) {
        String resultMessage;
        if (voiceChannel.getConnectedUsers().contains(by.getClient().getOurUser())) {
            resultMessage = "I'm already into this voice channel";
        } else if (voiceChannel.getUserLimit() > 0 && voiceChannel.getConnectedUsers().size() >= voiceChannel.getUserLimit()) {
            resultMessage = "Max users reached for voice channel " + voiceChannel.getName();
        } else {
            try {
                voiceChannel.join();
                resultMessage = "Joined voice channel " + voiceChannel.getName();
            } catch (MissingPermissionsException e) {
                resultMessage = "I don't have permissions to connect to voice channel " + voiceChannel.getName();
            }
        }

        MessageUtil.reply(message, resultMessage);
    }
}
