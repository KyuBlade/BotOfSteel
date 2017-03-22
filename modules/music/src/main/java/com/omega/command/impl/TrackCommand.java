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
import com.omega.util.MessageUtil;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.apache.commons.lang3.time.DurationFormatUtils;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
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
        if (audioPlayer != null) {
            AudioTrack playingTrack = audioPlayer.getPlayingTrack();
            if (playingTrack != null) {
                EmbedObject trackEmbed = getTrackEmbbed(playingTrack);
                if (trackEmbed != null) {
                    MessageUtil.reply(message, "Playing track : ", trackEmbed);
                } else {
                    MessageUtil.reply(message, "Audio source still not supported");
                }
            } else {
                MessageUtil.reply(message, "No tracks playing actually");
            }
        }
    }

    private EmbedObject getTrackEmbbed(AudioTrack track) {
        AudioSourceManager sourceManager = track.getSourceManager();
        EmbedObject embedObject = null;
        if (sourceManager instanceof YoutubeAudioSourceManager) {
            embedObject = getYoutubeEmbbed(track);
        } else {
            embedObject = null;
        }

        return embedObject;
    }

    private EmbedObject getYoutubeEmbbed(AudioTrack track) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.withTitle(track.getInfo().title)
            .withAuthorName(track.getInfo().author)
            .withUrl("https://youtube.com/watch?v=" + track.getIdentifier())
            .withDescription(
                DurationFormatUtils.formatDuration(track.getPosition(), "HH:mm:ss") + " / "
                    + DurationFormatUtils.formatDuration(track.getDuration(), "HH:mm:ss"));

        return builder.build();
    }
}
