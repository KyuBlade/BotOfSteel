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

@Command(name = "skip")
public class SkipCommand extends AbstractCommand {

    public SkipCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Signature(help = "Skip the current track")
    public void skipCommand() {
        skip(1);
    }

    @Signature(help = "Skip x tracks")
    public void skipCommand(@Parameter(name = "count") Long count) {
        skip(count.intValue());
    }

    private void skip(int count) {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildAudioPlayer audioPlayer = (GuildAudioPlayer) guildContext.getModuleComponent(MusicModule.AUDIO_PLAYER_COMPONENT);
        audioPlayer.skip(count);
    }
}
