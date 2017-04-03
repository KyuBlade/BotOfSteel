package com.omega.command.impl;

import com.omega.MusicModule;
import com.omega.MusicPermissionSupplier;
import com.omega.audio.GuildAudioPlayer;
import com.omega.command.*;
import com.omega.guild.GuildContext;
import com.omega.guild.GuildManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

@Command(name = "skip")
public class SkipCommand extends AbstractCommand {

    public SkipCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = MusicPermissionSupplier.COMMAND_QUEUE_SKIP)
    @Signature(help = "Skip the current track")
    public void skipCommand() {
        skip(1);
    }

    @Permission(permission = MusicPermissionSupplier.COMMAND_QUEUE_SKIP)
    @Signature(help = "Skip x tracks")
    public void skipCommand(@Parameter(name = "count") Long count) {
        skip(count.intValue());
    }

    private void skip(int count) {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildAudioPlayer audioPlayer = (GuildAudioPlayer) guildContext.getModuleComponent(MusicModule.AUDIO_PLAYER_COMPONENT);

        if (count > 0) {
            AudioTrack track = audioPlayer.getPlayingTrack();
            if (track != null) {
                audioPlayer.skip(count);

                if (count == 1) {
                    sendStateMessage("Skipped track " + track.getInfo().title);
                } else {
                    sendStateMessage("Skipped " + count + " tracks");
                }
            } else {
                sendStateMessage("Nothing to skip, no track playing");
            }
        }
    }
}
