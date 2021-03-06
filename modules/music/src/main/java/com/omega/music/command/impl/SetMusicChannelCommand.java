package com.omega.music.command.impl;

import com.omega.core.command.AbstractCommand;
import com.omega.core.command.CommandInfo;
import com.omega.core.command.Parameter;
import com.omega.core.command.Signature;
import com.omega.core.guild.GuildContext;
import com.omega.core.guild.GuildManager;
import com.omega.music.MusicModule;
import com.omega.music.audio.GuildAudioPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;

import java.awt.*;

@CommandInfo(name = "setmusicchannel", aliases = "smc")
public class SetMusicChannelCommand extends AbstractCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetMusicChannelCommand.class);

    public SetMusicChannelCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Signature(help = "Set your current voice channel as a music channel")
    public void setMusicChannelCommand() {
        sendStateMessage("Setting voice channel, please wait ...", Color.BLUE);

        IVoiceState voiceState = by.getVoiceStateForGuild(guild);
        IVoiceChannel currentVoiceChannel = voiceState.getChannel();

        if (currentVoiceChannel != null) {
            setMusicChannel(currentVoiceChannel);
        } else {
            sendErrorMessage("You are not in a voice channel");
        }
    }

    @Signature(help = "Set the specified voice channel as a music channel")
    public void setMusicChannelCommand(@Parameter(name = "voiceChannelName") String voiceChannelName) {
        message.getGuild().getVoiceChannels();
        IGuild guild = message.getGuild();

        IVoiceChannel voiceChannel = guild.getVoiceChannels()
            .stream()
            .filter(channel -> channel.getName().equalsIgnoreCase(voiceChannelName))
            .findFirst()
            .orElse(null);

        if (voiceChannel != null) {
            setMusicChannel(voiceChannel);
        } else {
            sendErrorMessage("Voice channel " + voiceChannelName + " not found");
        }
    }

    private void setMusicChannel(IVoiceChannel voiceChannel) {
        try {
            GuildContext guildContext = GuildManager.getInstance().getContext(voiceChannel.getGuild());
            GuildAudioPlayer audioPlayer = (GuildAudioPlayer) guildContext.getModuleComponent(MusicModule.AUDIO_PLAYER_COMPONENT);

            audioPlayer.setMusicChannel(voiceChannel);

            sendStateMessage("Voice channel " + voiceChannel.getName() + " is now a music channel");
        } catch (DiscordException e) {
            sendExceptionMessage("Failed to set voice channel " + voiceChannel.getName() + " as music channel", e);
        } catch (MissingPermissionsException e) {
            sendMissingPermissionsMessage(e.getMissingPermissions());
        }
    }
}
