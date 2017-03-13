package com.omega.command.impl;

import com.omega.MusicModule;
import com.omega.audio.GuildAudioPlayer;
import com.omega.audio.callback.QueueAudioLoadCallback;
import com.omega.command.AbstractCommand;
import com.omega.command.Command;
import com.omega.command.Parameter;
import com.omega.command.Signature;
import com.omega.guild.GuildContext;
import com.omega.guild.GuildManager;
import com.omega.util.MessageUtil;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.*;

import java.util.List;
import java.util.stream.IntStream;

@Command(name = "queue", aliases = "q")
public class QueueCommand extends AbstractCommand {

    public QueueCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Signature(help = "Get list of the 10 next tracks in the queue")
    public void queueCommand() {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildAudioPlayer audioPlayer = (GuildAudioPlayer) guildContext.getModuleComponent(MusicModule.AUDIO_PLAYER_COMPONENT);
        ;

        List<AudioTrack> queue = audioPlayer.getQueue();
        String resultMessage;
        if (!queue.isEmpty()) {
            StringBuilder sb = new StringBuilder(128);
            sb.append(MessageBuilder.Styles.CODE)
                .append("Tracks in queue : \n\n");

            final int TRACK_TO_DISPLAY = 10;
            IntStream.range(0, Math.min(TRACK_TO_DISPLAY, queue.size())).forEach(i -> {
                AudioTrack track = queue.get(i);
                sb.append(track.getInfo().title).append('\n');
            });

            int notShownTrackCount = queue.size() - 10;
            if (notShownTrackCount > 0) {
                sb.append("...").append('\n')
                    .append(notShownTrackCount).append(" more");
            }

            sb.append(MessageBuilder.Styles.CODE);
            resultMessage = sb.toString();
        } else {
            resultMessage = "No tracks in queue";
        }

        MessageUtil.reply(message, resultMessage);
    }

    @Signature(help = "Add the source track(s) to the queue.")
    public void queueCommand(@Parameter(name = "source") String source) {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildAudioPlayer audioPlayer = (GuildAudioPlayer) guildContext.getModuleComponent(MusicModule.AUDIO_PLAYER_COMPONENT);
        audioPlayer.queue(source, false, new QueueAudioLoadCallback(message));
    }
}
