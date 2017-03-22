package com.omega.command.impl;

import com.omega.MusicModule;
import com.omega.MusicPermissionSupplier;
import com.omega.audio.GuildAudioPlayer;
import com.omega.audio.callback.PlayAudioLoadCallback;
import com.omega.command.*;
import com.omega.guild.GuildContext;
import com.omega.guild.GuildManager;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

@Command(name = "play")
public class PlayCommand extends AbstractCommand {

    public PlayCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = MusicPermissionSupplier.COMMAND_PLAY)
    @Signature(help = "Put the source track(s) at the head of the queue and play it immediately")
    public void playCommand(@Parameter(name = "source") String source) {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildAudioPlayer audioPlayer = (GuildAudioPlayer) guildContext.getModuleComponent(MusicModule.AUDIO_PLAYER_COMPONENT);
        audioPlayer.play(source, new PlayAudioLoadCallback(message));
    }
}
