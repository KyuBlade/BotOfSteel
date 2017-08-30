package com.omega.music.command.impl;

import com.omega.core.command.*;
import com.omega.core.guild.GuildContext;
import com.omega.core.guild.GuildManager;
import com.omega.music.MusicModule;
import com.omega.music.MusicPermissionSupplier;
import com.omega.music.audio.GuildAudioPlayer;
import com.omega.music.audio.callback.PlayAudioLoadCallback;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;

@CommandInfo(name = "play")
public class PlayCommand extends AbstractCommand {

    public PlayCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = MusicPermissionSupplier.COMMAND_PLAY)
    @Signature(help = "Put the source track(s) at the head of the queue and play it immediately")
    public void playCommand(@Parameter(name = "source") String source) {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildAudioPlayer audioPlayer = (GuildAudioPlayer) guildContext.getModuleComponent(MusicModule.AUDIO_PLAYER_COMPONENT);

        sendStateMessage(
            new EmbedBuilder()
                .withDescription("Processing play command, please wait ...")
                .appendField("Source", source, false)
                .build(),
            Color.BLUE
        );

        audioPlayer.play(source, new PlayAudioLoadCallback(this));
    }
}
