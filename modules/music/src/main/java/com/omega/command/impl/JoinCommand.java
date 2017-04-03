package com.omega.command.impl;

import com.omega.MusicPermissionSupplier;
import com.omega.command.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;

@Command(name = "join", aliases = "j")
public class JoinCommand extends AbstractCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(JoinCommand.class);

    private final EmbedBuilder embedBuilder;

    public JoinCommand(IUser by, IMessage message) {
        super(by, message);

        this.embedBuilder = new EmbedBuilder();
    }

    @Permission(permission = MusicPermissionSupplier.COMMAND_JOIN)
    @Signature(help = "The bot will join your current voice channel")
    public void joinCommand() {
        IVoiceState voiceState = by.getVoiceStateForGuild(message.getGuild());
        IVoiceChannel connectedVoiceChannel = voiceState.getChannel();

        if (connectedVoiceChannel != null) {
            join(connectedVoiceChannel);
        } else {
            embedBuilder.withDescription("You are not connected to a voice channel");

            sendStateMessage(embedBuilder.build());
        }
    }

    @Permission(permission = MusicPermissionSupplier.COMMAND_JOIN)
    @Signature(help = "Bot will join the specified voice channel")
    public void joinCommand(@Parameter(name = "voiceChannelName") String voiceChannelName) {
        final IGuild guild = message.getGuild();
        IVoiceChannel voiceChannel = guild.getVoiceChannels().stream()
            .filter(channel -> channel.getName().equalsIgnoreCase(voiceChannelName)).findFirst().orElse(null);

        if (voiceChannel != null) {
            join(voiceChannel);
        } else {
            embedBuilder.withDescription("I don't see voice channel with name " + voiceChannelName);

            sendStateMessage(embedBuilder.build());
        }
    }

    private void join(IVoiceChannel voiceChannel) {
        if (voiceChannel.getConnectedUsers().contains(by.getClient().getOurUser())) {
            embedBuilder.withDescription("I'm already into this voice channel");
        } else if (voiceChannel.getUserLimit() > 0 && voiceChannel.getConnectedUsers().size() >= voiceChannel.getUserLimit()) {
            embedBuilder.withDescription("Max users reached for voice channel " + voiceChannel.getName());
        } else {
            try {
                voiceChannel.join();
                embedBuilder.withDescription("Joined voice channel " + voiceChannel.getName());
            } catch (MissingPermissionsException e) {
                sendMissingPermissionsMessage(
                    "I don't have permissions to connect to voice channel " + voiceChannel.getName(),
                    e.getMissingPermissions()
                );

                return;
            }
        }

        sendStateMessage(embedBuilder.build());
    }
}
