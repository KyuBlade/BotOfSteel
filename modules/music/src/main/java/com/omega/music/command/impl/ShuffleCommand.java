package com.omega.music.command.impl;

import com.omega.core.command.AbstractCommand;
import com.omega.core.command.CommandInfo;
import com.omega.core.command.Permission;
import com.omega.core.command.Signature;
import com.omega.core.guild.GuildContext;
import com.omega.core.guild.GuildManager;
import com.omega.music.MusicModule;
import com.omega.music.MusicPermissionSupplier;
import com.omega.music.audio.GuildAudioPlayer;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

@CommandInfo(name = "shuffle", aliases = "shfl")
public class ShuffleCommand extends AbstractCommand {

    public ShuffleCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = MusicPermissionSupplier.COMMAND_QUEUE_SHUFFLE)
    @Signature(help = "Shuffle the queue")
    public void shuffleCommand() {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildAudioPlayer audioPlayer = (GuildAudioPlayer) guildContext.getModuleComponent(MusicModule.AUDIO_PLAYER_COMPONENT);

        audioPlayer.shuffle();

        sendStateMessage("Queue as been shuffled");
    }
}
