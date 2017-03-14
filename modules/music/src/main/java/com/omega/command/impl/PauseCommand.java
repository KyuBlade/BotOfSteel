package com.omega.command.impl;

import com.omega.MusicModule;
import com.omega.audio.GuildAudioPlayer;
import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Parameter;
import com.omega.command.Signature;
import com.omega.guild.GuildContext;
import com.omega.guild.GuildManager;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

@Command(name = "pause", aliases = "p")
public class PauseCommand extends AbstractCommand {

    public PauseCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Signature(help = "Pause the audio player")
    public void pauseCommand() {
        pauseCommand(true);
    }

    @Signature(help = "Pause or resume the audio player, true to pause, false to resume")
    public void pauseCommand(@Parameter(name = "pause") Boolean pause) {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildAudioPlayer audioPlayer = (GuildAudioPlayer) guildContext.getModuleComponent(MusicModule.AUDIO_PLAYER_COMPONENT);
        boolean currentState = audioPlayer.isPause();
        boolean nextState = pause;
        if (currentState != nextState) {
            audioPlayer.pause(nextState);
        }
    }
}
