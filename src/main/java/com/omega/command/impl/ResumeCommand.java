package com.omega.command.impl;

import com.omega.audio.GuildAudioPlayer;
import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Signature;
import com.omega.guild.GuildContext;
import com.omega.guild.GuildManager;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

@Command(name = "resume", aliases = "rsm")
public class ResumeCommand extends AbstractCommand {

    public ResumeCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Signature(help = "Resume audio player")
    public void resumeCommand() {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildAudioPlayer audioPlayer = guildContext.getAudioPlayer();

        audioPlayer.pause(false);
    }
}
