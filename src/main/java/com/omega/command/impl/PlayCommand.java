package com.omega.command.impl;

import com.omega.audio.GuildAudioPlayer;
import com.omega.audio.callback.PlayAudioLoadCallback;
import com.omega.audio.callback.QueueAudioLoadCallback;
import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Parameter;
import com.omega.command.Signature;
import com.omega.guild.GuildContext;
import com.omega.guild.GuildManager;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

@Command(name = "play")
public class PlayCommand extends AbstractCommand {

    public PlayCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Signature(help = "Put the source track(s) at the head of the queue and play it immediately")
    public void playCommand(@Parameter(name = "source") String source) {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildAudioPlayer audioPlayer = guildContext.getAudioPlayer();
        audioPlayer.play(source, new PlayAudioLoadCallback(message));
    }
}
