package com.omega.command.impl;

import com.omega.MusicModule;
import com.omega.audio.GuildAudioPlayer;
import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Parameter;
import com.omega.command.Signature;
import com.omega.guild.GuildContext;
import com.omega.guild.GuildManager;
import com.omega.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

@Command(name = "setmusicchannel", aliases = "smc")
public class SetMusicChannelCommand extends AbstractCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetMusicChannelCommand.class);

    public SetMusicChannelCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Signature(help = "Set your current voice channel as a music channel")
    public void setMusicChannelCommand() {
        IVoiceState voiceState = by.getVoiceStateForGuild(message.getGuild());
        IVoiceChannel currentVoiceChannel = voiceState.getChannel();
        if (currentVoiceChannel != null) {
            setMusicChannel(currentVoiceChannel);
        } else {
            MessageUtil.reply(message, "You are not in a voice channel");
        }
    }

    @Signature(help = "Set the specified voice channel as a music channel")
    public void setMusicChannelCommand(@Parameter(name = "voiceChannelName") String voiceChannelName) {
        message.getGuild().getVoiceChannels();
        IGuild guild = message.getGuild();
        IVoiceChannel voiceChannel = guild.getVoiceChannels().stream()
            .filter(channel -> channel.getName().equalsIgnoreCase(voiceChannelName)).findFirst().orElse(null);
        if (voiceChannel != null) {
            setMusicChannel(voiceChannel);
        } else {
            MessageUtil.reply(message, "Voice channel " + voiceChannelName + " not found");
        }
    }

    private void setMusicChannel(IVoiceChannel voiceChannel) {
        try {
            GuildContext guildContext = GuildManager.getInstance().getContext(voiceChannel.getGuild());
            GuildAudioPlayer audioPlayer = (GuildAudioPlayer) guildContext.getModuleComponent(MusicModule.AUDIO_PLAYER_COMPONENT);
            audioPlayer.setMusicChannel(voiceChannel);
            MessageUtil.reply(message, "Voice channel " + voiceChannel.getName() + " is now a music channel");
        } catch (DiscordException e) {
            LOGGER.error("Failed to set voice channel " + voiceChannel.getName() + " as music channel", e);
        } catch (MissingPermissionsException e) {
            LOGGER.warn("Missing permissions", e);
            MessageUtil.reply(message, "Permissions needed");
        } catch (RateLimitException e) {
            LOGGER.warn("Rate limit exceeded", e);
        }
    }
}
