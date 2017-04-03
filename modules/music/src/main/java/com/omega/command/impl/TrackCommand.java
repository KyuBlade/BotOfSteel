package com.omega.command.impl;

import com.omega.MusicModule;
import com.omega.MusicPermissionSupplier;
import com.omega.audio.GuildAudioPlayer;
import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Permission;
import com.omega.command.Signature;
import com.omega.guild.GuildContext;
import com.omega.guild.GuildManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.apache.commons.lang3.time.DurationFormatUtils;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

@Command(name = "track")
public class TrackCommand extends AbstractCommand {

    public TrackCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = MusicPermissionSupplier.COMMAND_TRACK)
    @Signature(help = "Get the current playing track")
    public void trackCommand() {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildAudioPlayer audioPlayer = (GuildAudioPlayer) guildContext.getModuleComponent(MusicModule.AUDIO_PLAYER_COMPONENT);

        EmbedBuilder embedBuilder = new EmbedBuilder();

        if (audioPlayer != null) {
            AudioTrack playingTrack = audioPlayer.getPlayingTrack();

            if (playingTrack != null) {
                embedBuilder
                    .withAuthorName(playingTrack.getInfo().author)
                    .withTitle(playingTrack.getInfo().title)
                    .withUrl(playingTrack.getInfo().uri)
                    .appendField(
                        "Duration",
                        DurationFormatUtils.formatDuration(playingTrack.getPosition(), "HH:mm:ss") + " / "
                            + DurationFormatUtils.formatDuration(playingTrack.getDuration(), "HH:mm:ss"),
                        true
                    )
                    .appendField("State", playingTrack.getState().name(), true)
                    .appendField("Stream", (playingTrack.getInfo().isStream) ? "Yes" : "No", true)
                    .appendField("Seekable", (playingTrack.isSeekable()) ? "Yes" : "No", true);
            } else {
                embedBuilder.withDescription("No track playing actually");
            }
        }

        sendStateMessage(embedBuilder.build());
    }
}
