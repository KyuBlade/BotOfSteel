package com.omega.command.impl;

import com.omega.audio.GuildAudioPlayer;
import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Signature;
import com.omega.guild.GuildContext;
import com.omega.guild.GuildManager;
import com.omega.util.SenderUtil;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

@Command(name = "shuffle", aliases = "shfl")
public class ShuffleCommand extends AbstractCommand {

    public ShuffleCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Signature(help = "Shuffle the queue")
    public void shuffleCommand() {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildAudioPlayer audioPlayer = guildContext.getAudioPlayer();

        audioPlayer.shuffle();
        SenderUtil.reply(message, "Queue as been shuffled");
    }
}
