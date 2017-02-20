package com.omega.command.impl;

import com.omega.BotManager;
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
        if (canSkip()) {
            GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
            GuildAudioPlayer audioPlayer = guildContext.getAudioPlayer();
            audioPlayer.skip(count);
        }
    }

    private boolean canSkip() {
        return !message.getGuild().getID().equals("127824775090405377") ||
            BotManager.getInstance().getApplicationOwner().getID().equals(by.getID());
    }
}
