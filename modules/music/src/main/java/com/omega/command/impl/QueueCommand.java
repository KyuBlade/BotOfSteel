package com.omega.command.impl;

import com.omega.MusicModule;
import com.omega.MusicPermissionSupplier;
import com.omega.audio.GuildAudioPlayer;
import com.omega.audio.callback.QueueAudioLoadCallback;
import com.omega.command.*;
import com.omega.guild.GuildContext;
import com.omega.guild.GuildManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.util.List;
import java.util.stream.IntStream;

@Command(name = "queue", aliases = "q")
public class QueueCommand extends AbstractCommand {

    public QueueCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = MusicPermissionSupplier.COMMAND_QUEUE_LIST)
    @Signature(help = "Get list of the 10 next tracks in the queue")
    public void queueCommand() {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildAudioPlayer audioPlayer = (GuildAudioPlayer) guildContext.getModuleComponent(MusicModule.AUDIO_PLAYER_COMPONENT);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        List<AudioTrack> queue = audioPlayer.getQueue();

        if (!queue.isEmpty()) {
            embedBuilder.withDescription("Tracks in queue :\n\n");

            IntStream.range(0, Math.min(10, queue.size())).forEach(i -> {
                AudioTrack track = queue.get(i);
                embedBuilder.appendField((i + 1) + " - " + track.getInfo().title, track.getInfo().uri, false);
            });

            int notShownTrackCount = queue.size() - 10;
            if (notShownTrackCount > 0) {
                embedBuilder.appendField("...", notShownTrackCount + " more", false);
            }
        } else {
            embedBuilder.appendField("No tracks in queue.",
                "Use \"queue url\" to add tracks to the queue.",
                false
            );
        }

        sendStateMessage(embedBuilder.build());
    }

    @Permission(permission = MusicPermissionSupplier.COMMAND_QUEUE_ADD)
    @Signature(help = "Add the source track(s) to the queue.")
    public void queueCommand(@Parameter(name = "source") String source) {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildAudioPlayer audioPlayer = (GuildAudioPlayer) guildContext.getModuleComponent(MusicModule.AUDIO_PLAYER_COMPONENT);

        sendStateMessage(
            new EmbedBuilder()
                .withDescription("Processing the source, please wait ...")
                .build(),
            Color.BLUE
        );

        audioPlayer.queue(source, false, new QueueAudioLoadCallback(this));
    }
}
